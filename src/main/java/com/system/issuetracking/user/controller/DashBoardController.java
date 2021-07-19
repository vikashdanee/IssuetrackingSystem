package com.system.issuetracking.user.controller;

import com.system.issuetracking.service.IssueService;
import com.system.issuetracking.user.model.User;
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

import java.util.List;


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
        return "dashboard";
    }

    private User getLoggedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails)auth.getPrincipal();
        return myUserDetails.getUser();
    }
}
