package com.system.issuetracking.user.controller;


import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/home")
@AllArgsConstructor
public class HomeController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public String registerUser(ModelMap map) {
        return "home";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "user";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user) {

        userService.save(user);

        return "user";
    }


}
