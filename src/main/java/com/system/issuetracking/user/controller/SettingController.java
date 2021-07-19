package com.system.issuetracking.user.controller;


import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.issue.model.IssueComment;
import com.system.issuetracking.user.model.Role;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.service.MyUserDetails;
import com.system.issuetracking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/setting")
@AllArgsConstructor
public class SettingController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public String getSettingPage(Model model) {
        User user = getLoggedUser();
        model.addAttribute("user",user);
        return "setting";
    }

    @PostMapping("/update-account/{id}")
    private String getSaveUser(@PathVariable Long id, @ModelAttribute User user, ModelMap map,Model model){
        User existedUser = userService.findById(id);
        if(existedUser==null){
            existedUser = new User();
        }
        existedUser.setFirstName(user.getFirstName());
        existedUser.setLastName(user.getLastName());
        existedUser.setEmail(user.getEmail());
        existedUser.setPassword(user.getPassword());
        user = userService.save(existedUser);
        model.addAttribute("user",user);
        if(user !=null){
            map.put("msg","Account information updated!");
            return "setting";
        }
        else {
            map.put("msg","Account information update fail");
            return "setting";
        }
    }

    private User getLoggedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        MyUserDetails myUserDetails= (MyUserDetails)auth.getPrincipal();
        return myUserDetails.getUser();
    }
}
