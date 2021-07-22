package com.system.issuetracking.issue.controller;


import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.issue.model.IssueComment;
import com.system.issuetracking.service.IssueService;
import com.system.issuetracking.user.controller.UserController;
import com.system.issuetracking.user.model.Role;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.model.UserNotification;
import com.system.issuetracking.user.service.MyUserDetails;
import com.system.issuetracking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(IssueController.URI)
@AllArgsConstructor
@SuppressWarnings("Duplicates")
public class IssueController {

    public static final String URI = "/issues";

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        webDataBinder
                .registerCustomEditor(Date.class, new CustomDateEditor(simpleDateFormat, true));
    }

    @GetMapping
    public String getIssueListPage(ModelMap map) {
        List<Issue> issues = issueService.findAllIssue();
        map.put("issues", issues);
        return "issues";
    }

    @GetMapping("/{action}")
    public String getUserIssueListPage(@PathVariable String action,ModelMap map,Model model) {

        User user = getLoggedUser();
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        List<Issue> issues;
        switch (action) {
            case "assigned":
                issues = issueService.findAllIssueByUserAndStatus(user,"Assigned");
                map.put("issues", issues);
                return "issues";

            case "in-progress":
                issues = issueService.findAllIssueByUserAndStatus(user,"In-progress");
                map.put("issues", issues);
                return "issues";

                case "completed":
                    issues = issueService.findAllIssueByUserAndStatus(user,"Completed");
                    map.put("issues", issues);
                    return "issues";

            case "approved":
                issues = issueService.findAllIssueByUserAndStatus(user,"Approved");
                map.put("issues", issues);
                return "issues";

            default:
                issues = issueService.findAllIssue();
                map.put("issues", issues);
                return "issues";
        }
    }

    @GetMapping("/issue")
    public String getIssueCreatePage(Model model) {
        return "issue";
    }

    @GetMapping("/issue-detail/{id}")
    public String getIssueDetailPage(@PathVariable Long id,Model model) {
        User user = getLoggedUser();
        Issue issue = issueService.findById(id);
        List<UserNotification> notifications = issueService.findNotificationByIssueId(id,user,true);
        for(UserNotification userNotification:notifications){
            userNotification.setStillActive(false);
            issueService.saveNotification(userNotification);
        }
        List<IssueComment> comments = issueService.findAllIssueComments(issue);
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("issue", issue);
        model.addAttribute("comments", comments);
        model.addAttribute("user",user);
        return "issuedetail";
    }

    @GetMapping("/issue-detail-update/{id}")
    public String getIssueDetailUpdatePage(@PathVariable Long id,Model model) {
        Issue issue = issueService.findById(id);
        List<User> users = userService.findUsersByDestination("Developer");
        model.addAttribute("issue", issue);
        model.addAttribute("users",users);
        return "issueUpdate";
    }

    @PostMapping("/issue-update/{id}")
    public String getIssueUpdate(@PathVariable Long id,@ModelAttribute Issue issue,Model model) throws MessagingException {
        Issue existedIssue = issueService.findById(id);
        if(existedIssue==null)
            existedIssue = new Issue();
        existedIssue.setTitle(issue.getTitle());
        existedIssue.setPurposeOfReq(issue.getPurposeOfReq());
        existedIssue.setDesignDetail(issue.getDesignDetail());
        existedIssue.setSystemModule(issue.getSystemModule());
        existedIssue.setWorkType(issue.getWorkType());
        existedIssue.setPriority(issue.getPriority());
     //   existedIssue.setAssignTo(issue.getAssignTo());
        existedIssue.setNotLaterThan(issue.getNotLaterThan());
        issueService.update(existedIssue);
        return "redirect:/issues";
    }

    @GetMapping("/mark-as-inprogress/{id}")
    public String markIssueAsInProgress(@PathVariable Long id,Model model) throws MessagingException {
        User user = getLoggedUser();
        Issue issue = issueService.findById(id);
        issue.setStatus("In-progress");
        issue = issueService.update(issue);

        List<IssueComment> comments = issueService.findAllIssueComments(issue);
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("issue", issue);
        model.addAttribute("comments", comments);
        model.addAttribute("user",user);
        return "issuedetail";
    }

    @GetMapping("/mark-as-testing/{id}")
    public String markIssueAsUnderTest(@PathVariable Long id,Model model) throws MessagingException {
        User user = getLoggedUser();
        Issue issue = issueService.findById(id);
        issue.setStatus("Completed");
        issue = issueService.maskAsTest(issue);

        List<IssueComment> comments = issueService.findAllIssueComments(issue);
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("issue", issue);
        model.addAttribute("comments", comments);
        model.addAttribute("user",user);
        return "issuedetail";
    }

    @GetMapping("/mark-as-incomplete/{id}")
    public String markIssueAsReAssigned(@PathVariable Long id,Model model) throws MessagingException {
        User user = getLoggedUser();
        Issue issue = issueService.findById(id);
        issue.setStatus("Assigned");
        issue = issueService.reAssigned(issue);

        List<IssueComment> comments = issueService.findAllIssueComments(issue);
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("issue", issue);
        model.addAttribute("comments", comments);
        model.addAttribute("user",user);
        return "issuedetail";
    }

    @GetMapping("/mark-as-complete/{id}")
    public String markIssueAsCompleted(@PathVariable Long id,Model model) throws MessagingException {
        User user = getLoggedUser();
        Issue issue = issueService.findById(id);
        issue.setStatus("Approved");
        issue = issueService.maskAsCompleted(issue);

        List<IssueComment> comments = issueService.findAllIssueComments(issue);
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("issue", issue);
        model.addAttribute("comments", comments);
        model.addAttribute("user",user);
        return "issuedetail";
    }

    @PostMapping("/issue")
    private String getSaveIssue(@ModelAttribute Issue issue, ModelMap map) throws MessagingException {
        issue = issueService.save(issue);
        if(issue !=null){
            map.put("msg","Issue successfully created and assigned to "+issue.getAssignTo().getFirstName());
            return "redirect:/issues";
        }
        else {
            map.put("msg","No Developer is available at this time");
            return "issue";
        }
    }

    @GetMapping("/edit/{id}")
    public ModelAndView updateIssue(@PathVariable Long id){
        Issue issue = issueService.findById(id);
        ModelAndView modelAndView = new ModelAndView("issue");
        modelAndView.addObject("issue", issue);
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        issueService.deleteById(id);
        return "redirect:/issues";
    }

    @PostMapping("/add-comment/{id}")
    private String addIssueComment(@PathVariable Long id, @ModelAttribute IssueComment comment, ModelMap map,Model model) {
        User user = getLoggedUser();
        Issue issue = issueService.findById(id);
        comment.setCommentDate(new Date());
        comment.setIssue(issue);
        comment.setCommentBy(user.getEmail());
        comment = issueService.saveComment(comment);
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        List<IssueComment> comments = issueService.findAllIssueComments(issue);
        model.addAttribute("issue", issue);
        model.addAttribute("comments", comments);
        model.addAttribute("user",user);
        if(comment !=null){
            map.put("msg","Comment successfully added.");
            return "issuedetail";
        }
        else {
            map.put("msg","No Developer is available at this time");
            return "issuedetail";
        }
    }

    @PostMapping("/update-percentage/{id}")
    private String addIssuePercentage(@PathVariable Long id, @ModelAttribute Issue issue, ModelMap map,Model model) throws MessagingException {
        User user = getLoggedUser();
        Issue savedIssue = issueService.findById(id);
        savedIssue.setRemedyMadeExplanation(issue.getRemedyMadeExplanation());
        savedIssue.setPercentageComplete(issue.getPercentageComplete());

        savedIssue = issueService.update(savedIssue);
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        List<IssueComment> comments = issueService.findAllIssueComments(savedIssue);
        model.addAttribute("issue", savedIssue);
        model.addAttribute("comments", comments);
        model.addAttribute("user",user);
        if(savedIssue !=null){
            map.put("msg","Comment successfully added.");
            return "issuedetail";
        }
        else {
            map.put("msg","No Developer is available at this time");
            return "issuedetail";
        }
    }

    @GetMapping("/delete-comment/{id}/{cId}")
    private String removeIssueComment(@PathVariable Long id,@PathVariable Long cId,Model model) {
        issueService.deleteCommentById(cId);
        Issue issue = issueService.findById(id);
        User user = getLoggedUser();
        List<IssueComment> comments = issueService.findAllIssueComments(issue);
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("issue", issue);
        model.addAttribute("comments", comments);
        model.addAttribute("user",user);
        return "issuedetail";

    }

    private User getLoggedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails)auth.getPrincipal();
        return myUserDetails.getUser();
    }
}
