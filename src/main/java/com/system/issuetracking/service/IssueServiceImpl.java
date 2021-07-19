package com.system.issuetracking.service;

import com.system.issuetracking.entity.EmailTemplate;
import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.issue.model.IssueComment;
import com.system.issuetracking.repository.CommentRepository;
import com.system.issuetracking.repository.IssueRepository;
import com.system.issuetracking.repository.NotificationRepository;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.model.UserNotification;
import com.system.issuetracking.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IssueServiceImpl implements IssueService{

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private NotificationRepository notificationRepository;


    @Override
    public Issue save(Issue issue) throws MessagingException {
        User userAssigned= null;
        userAssigned = getPreviousIssueDeveloper(issue);
       if(userAssigned==null){
           userAssigned=getAvailableDeveloper(issue);
       }
       if(userAssigned!=null){
       issue.setAssignTo(userAssigned);
       issue.setPrepareDate(new Date());
       issue.setRequestNo(getNewRequestNumber());
       issue.setStatus("Assigned");
        Issue savedIssue = issueRepository.save(issue);
            sendNotification(savedIssue,"assigned to you.");
            storeNotification(savedIssue,false);
            return savedIssue;
        }
        return null;
    }

    @Override
    public List<Issue> findAllIssue() {
        return issueRepository.findAll();
    }

    @Override
    public List<Issue> findAllIssueByUser(User user) {
        return issueRepository.getIssuesByUser(user);
    }

    @Override
    public List<Issue> findAllIssueByUserAndStatus(User user, String status) {
        return issueRepository.getIssuesByUserAndStatus(user,status);
    }

    @Override
    public List<Issue> findAllIssueByStatus(String status) {
        return issueRepository.getIssuesByStatus(status);
    }

    @Override
    public List<UserNotification> findUserNotification(User user, boolean stillActive) {
        return notificationRepository.getNotifications(user,stillActive);
    }

    @Override
    public void deleteCommentById(long id) {
        commentRepository.deleteById(id);
    }

    @Override
    public void deleteById(long id) {
        issueRepository.deleteById(id);
    }

    @Override
    public Issue findById(long id) {
        return issueRepository.getById(id);
    }

    @Override
    public Issue update(Issue issue) throws MessagingException {
        Issue savedIssue = issueRepository.save(issue);
        sendNotification(savedIssue,"updated.");
        storeNotification(savedIssue,true);
        return savedIssue;
    }

    @Override
    public UserNotification findNotificationByIssueId(Long id,User user, boolean stillActive) {
        return notificationRepository.getNotification(id,user,stillActive);
    }

    @Override
    public UserNotification saveNotification(UserNotification userNotification) {
        return notificationRepository.save(userNotification);
    }

    @Override
    public IssueComment saveComment(IssueComment issueComment) {
        return commentRepository.save(issueComment);
    }

    @Override
    public List<IssueComment> findAllIssueComments(Issue issue) {
        return commentRepository.getIssueComments(issue);
    }

    public int getNewRequestNumber(){
        return issueRepository.getRequestNumber()==null?1:issueRepository.getRequestNumber().getRequestNo()+1;
    }

    public void sendNotification(Issue issue,String action) throws MessagingException {
        String subject="Issue IT-"+issue.getRequestNo()+" has been "+action;
        String body="<p>Following issue has been "+action+".</p>"
                +"<p>Title : "+issue.getTitle()+"</p>"
                +"<p>Design Detail: "+issue.getDesignDetail()+"</p>";
        EmailTemplate emailTemplate = new EmailTemplate(issue.getAssignTo().getFirstName(),body,"");

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(issue.getAssignTo().getEmail());
        helper.setSubject(subject);
        helper.setText(emailTemplate.returnContent(), true);
        javaMailSender.send(msg);
    }

    public void storeNotification(Issue issue,boolean update){
        UserNotification userNotification = new UserNotification();
        userNotification.setDate(new Date());
        if(update)
            userNotification.setNotification("Issue IT-"+issue.getRequestNo()+" has been updated.");
        else
            userNotification.setNotification("New Issue IT-"+issue.getRequestNo()+" assigned to you.");
        userNotification.setStillActive(true);
        userNotification.setUser(issue.getAssignTo());
        userNotification.setIssueId(issue.getId());
        notificationRepository.save(userNotification);
    }

    public User getPreviousIssueDeveloper(Issue issue){
        User assignedUser=null;
        if(issue.getRelatedIssue()!=null
                && issue.getRelatedIssue()
                && issue.getPreviousIssue()!=null){
            Issue previousIssue = issueRepository.getIssueByRequestNo(issue.getPreviousIssue());
            if(previousIssue!=null && previousIssue.getAssignTo()!=null)
                assignedUser=previousIssue.getAssignTo();
        }
        return assignedUser;
    }

    /* retrieve developer */
    public User getAvailableDeveloper(Issue issue){
        List<User> users = userRepository.getUsersByDesignation("Developer");

        /* check if user list is empty */
        if(users==null || users.isEmpty())
            return null;

        /* check if there is only one developer */
        else if(users.size()==1)
            return users.get(0);
        else{
            User avlUser=users.get(0);
            for(User user:users){
                List<Issue> assignedUserIssues = issueRepository.getIssuesByUserAndStatus(avlUser,"Assigned");
                List<Issue> assignedIssues = issueRepository.getIssuesByUserAndStatus(user,"Assigned");
                List<Issue> inProgressIssues = issueRepository.getIssuesByUserAndStatus(user,"In-progress");
                if(assignedIssues==null || assignedIssues.isEmpty()){
                    if(inProgressIssues==null || inProgressIssues.isEmpty()){
                        avlUser=user;
                    }else {
                        List<Issue> inProgressUserIssues = issueRepository.getIssuesByUserAndStatus(avlUser,"In-progress");
                        if(inProgressUserIssues!=null && !inProgressUserIssues.isEmpty()){
                            if(inProgressUserIssues.size()>inProgressIssues.size()){
                                avlUser=user;
                            }
                           else if(inProgressUserIssues.size()==inProgressIssues.size()){
                                Issue latestUserDueDateIssue = inProgressUserIssues.stream().min(Comparator.comparing(Issue::getNotLaterThan)).get();
                                Issue latestDueDateIssue = inProgressIssues.stream().min(Comparator.comparing(Issue::getNotLaterThan)).get();
                            if(latestDueDateIssue.getNotLaterThan().before(latestUserDueDateIssue.getNotLaterThan())){
                                avlUser=user;
                                }
                           }
                        }
                    }
                }
                else {
                    if(assignedUserIssues!=null && !assignedUserIssues.isEmpty()){
                        List<Issue> highPriorityIssues = assignedIssues
                                .stream()
                                .filter(issue1 -> issue1.getPriority().equalsIgnoreCase("Showstopper"))
                                .collect(Collectors.toList());
                        List<Issue> highPriorityUserIssues = assignedUserIssues
                                .stream()
                                .filter(issue1 -> issue1.getPriority().equalsIgnoreCase("Showstopper"))
                                .collect(Collectors.toList());
                        if(highPriorityUserIssues.size()>highPriorityIssues.size()){
                            avlUser=user;
                        }
                        else if(highPriorityUserIssues.size()==highPriorityIssues.size() && highPriorityIssues.size()>0){
                            Issue latestUserDueDateIssue = highPriorityUserIssues.stream().min(Comparator.comparing(Issue::getNotLaterThan)).get();
                            Issue latestDueDateIssue = highPriorityIssues.stream().min(Comparator.comparing(Issue::getNotLaterThan)).get();
                            if(latestDueDateIssue.getNotLaterThan().before(latestUserDueDateIssue.getNotLaterThan())){
                                avlUser=user;
                            }
                        }
                    }
                }
            }
            return avlUser;
        }
    }
}
