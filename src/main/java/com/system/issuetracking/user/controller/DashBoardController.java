package com.system.issuetracking.user.controller;

import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.service.IssueService;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.model.UserDetail;
import com.system.issuetracking.user.model.UserNotification;
import com.system.issuetracking.user.service.MyUserDetails;
import com.system.issuetracking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("Duplicates")
@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashBoardController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getDashBoard(Model model) {
        User user = getLoggedUser();
        List<UserNotification> userNotifications = issueService.findUserNotification(user,true);
        model.addAttribute("userNotifications", userNotifications);
        model.addAttribute("user",user);
        if(user.getRoles().isEmpty() || user.getRoles().stream().findFirst().get().getName().equalsIgnoreCase("User"))
            loadInitialData(model,user);
        else
            loadAdminInitialData(model);
        return "dashboard";
    }

    private User getLoggedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails)auth.getPrincipal();
        return myUserDetails.getUser();
    }

    private void loadInitialData(Model model,User user){
        List<Issue> showstopperIssues = issueService.findAllIssueByUserAndPriority(user,"Showstopper");
        List<Issue> allIssues = issueService.findAllIssueByUser(user);
        List<Issue> assignedIssues = issueService.findAllIssueByUserAndStatus(user,"Assigned");
        List<Issue> inProgressIssues = issueService.findAllIssueByUserAndStatus(user,"In-progress");
        List<Issue> testIssues= issueService.findAllIssueByUserAndStatus(user,"Completed");
        List<Issue> completedIssues = issueService.findAllIssueByUserAndStatus(user,"Approved");

        model.addAttribute("showstopperIssues",showstopperIssues);
        model.addAttribute("assignedIssues",assignedIssues);
        model.addAttribute("inProgressIssues",inProgressIssues);
        model.addAttribute("testIssues",testIssues);
        model.addAttribute("completedIssues",completedIssues);
        model.addAttribute("allIssues",allIssues);

        int assignedIssuesCount =assignedIssues.size();
        int inProgressIssuesCount =inProgressIssues.size();
        int testIssuesCount =testIssues.size();
        int completedIssuesCount =completedIssues.size();
        int showstopperIssuesCount =showstopperIssues.size();

        model.addAttribute("showstopperIssuesCount",showstopperIssuesCount);
        model.addAttribute("assignedIssuesCount",assignedIssuesCount);
        model.addAttribute("inProgressIssuesCount",inProgressIssuesCount);
        model.addAttribute("testIssuesCount",testIssuesCount);
        model.addAttribute("completedIssuesCount",completedIssuesCount);

        int totalIssues=assignedIssuesCount+testIssuesCount+completedIssuesCount+inProgressIssuesCount;

        int assignedIssuesCountPercentage=0,inProgressIssuesCountPercentage=0,testIssuesCountPercentage=0,completedIssuesCountPercentage=0;
        if(totalIssues!=0){
            assignedIssuesCountPercentage=(assignedIssuesCount*100)/totalIssues;
            inProgressIssuesCountPercentage=(inProgressIssuesCount*100)/totalIssues;
            testIssuesCountPercentage=(testIssuesCount*100)/totalIssues;
            completedIssuesCountPercentage=(completedIssuesCount*100)/totalIssues;
        }

        model.addAttribute("assignedIssuesCountPercentage",assignedIssuesCountPercentage+"%");
        model.addAttribute("inProgressIssuesCountPercentage",inProgressIssuesCountPercentage+"%");
        model.addAttribute("testIssuesCountPercentage",testIssuesCountPercentage+"%");
        model.addAttribute("completedIssuesCountPercentage",completedIssuesCountPercentage+"%");  List<User> users = userService.findUsersByDestination("Developer");

        List<UserDetail> userDetails = new ArrayList<>();
            UserDetail userDetail = new UserDetail();
            userDetail.setUser(user);
            List<Issue> assignedUserIssues = issueService.findAllIssueByUserAndStatus(user,"Assigned");
            List<Issue> inProgressUserIssues = issueService.findAllIssueByUserAndStatus(user,"In-progress");
            List<Issue> testUserIssues= issueService.findAllIssueByUserAndStatus(user,"Completed");
            List<Issue> completedUserIssues = issueService.findAllIssueByUserAndStatus(user,"Approved");
            userDetail.setAssigned(assignedUserIssues.size());
            userDetail.setProgress(inProgressUserIssues.size());
            userDetail.setTest(testUserIssues.size());
            userDetail.setCompleted(completedUserIssues.size());
            userDetail.setTotal(assignedUserIssues.size()+inProgressUserIssues.size()+testUserIssues.size()+completedUserIssues.size());

            userDetails.add(userDetail);


        model.addAttribute("userDetails",userDetails);


    }

    private void loadAdminInitialData(Model model){
        List<Issue> showstopperIssues = issueService.findAllIssueByStatus("Showstopper");
        List<Issue> allIssues = issueService.findAllIssueByStatus("Showstopper");
        List<Issue> assignedIssues = issueService.findAllIssueByStatus("Assigned");
        List<Issue> inProgressIssues = issueService.findAllIssueByStatus("In-progress");
        List<Issue> testIssues= issueService.findAllIssueByStatus("Completed");
        List<Issue> completedIssues = issueService.findAllIssueByStatus("Approved");

        model.addAttribute("showstopperIssues",showstopperIssues);
        model.addAttribute("assignedIssues",assignedIssues);
        model.addAttribute("inProgressIssues",inProgressIssues);
        model.addAttribute("testIssues",testIssues);
        model.addAttribute("completedIssues",completedIssues);
        model.addAttribute("allIssues",allIssues);

        int assignedIssuesCount =assignedIssues.size();
        int inProgressIssuesCount =inProgressIssues.size();
        int testIssuesCount =testIssues.size();
        int completedIssuesCount =completedIssues.size();
        int showstopperIssuesCount =showstopperIssues.size();

        model.addAttribute("showstopperIssuesCount",showstopperIssuesCount);
        model.addAttribute("assignedIssuesCount",assignedIssuesCount);
        model.addAttribute("inProgressIssuesCount",inProgressIssuesCount);
        model.addAttribute("testIssuesCount",testIssuesCount);
        model.addAttribute("completedIssuesCount",completedIssuesCount);

        int totalIssues=assignedIssuesCount+testIssuesCount+completedIssuesCount+inProgressIssuesCount;

        int assignedIssuesCountPercentage=0,inProgressIssuesCountPercentage=0,testIssuesCountPercentage=0,completedIssuesCountPercentage=0;
        if(totalIssues!=0){
            assignedIssuesCountPercentage=(assignedIssuesCount*100)/totalIssues;
            inProgressIssuesCountPercentage=(inProgressIssuesCount*100)/totalIssues;
            testIssuesCountPercentage=(testIssuesCount*100)/totalIssues;
            completedIssuesCountPercentage=(completedIssuesCount*100)/totalIssues;
        }

        model.addAttribute("assignedIssuesCountPercentage",assignedIssuesCountPercentage+"%");
        model.addAttribute("inProgressIssuesCountPercentage",inProgressIssuesCountPercentage+"%");
        model.addAttribute("testIssuesCountPercentage",testIssuesCountPercentage+"%");
        model.addAttribute("completedIssuesCountPercentage",completedIssuesCountPercentage+"%");

        List<User> users = userService.findUsersByDestination("Developer");
        List<UserDetail> userDetails = new ArrayList<>();
        for(User user: users){
            UserDetail userDetail = new UserDetail();
            userDetail.setUser(user);
            List<Issue> assignedUserIssues = issueService.findAllIssueByUserAndStatus(user,"Assigned");
            List<Issue> inProgressUserIssues = issueService.findAllIssueByUserAndStatus(user,"In-progress");
            List<Issue> testUserIssues= issueService.findAllIssueByUserAndStatus(user,"Completed");
            List<Issue> completedUserIssues = issueService.findAllIssueByUserAndStatus(user,"Approved");
            userDetail.setAssigned(assignedUserIssues.size());
            userDetail.setProgress(inProgressUserIssues.size());
            userDetail.setTest(testUserIssues.size());
            userDetail.setCompleted(completedUserIssues.size());
            userDetail.setTotal(assignedUserIssues.size()+inProgressUserIssues.size()+testUserIssues.size()+completedUserIssues.size());

            userDetails.add(userDetail);
        }

        model.addAttribute("userDetails",userDetails);
    }
}
