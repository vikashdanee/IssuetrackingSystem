package com.system.issuetracking.user.controller;

import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Monica Rajbhandari on 7/8/21
 */

@Controller
@RequestMapping("/login")
@AllArgsConstructor
public class LoginController {

    @Autowired
    private final UserService userService;

    @GetMapping
    public String getLogin(ModelMap map) {
        return "login";
    }

    @PostMapping
    public String login(@ModelAttribute User user, ModelMap map) {
        User newUser  = userService.checkUserExist(user);
        if (null != newUser) {
            return "dashboard";
        }else {
            map.put("msg", "Invalid email or password!");
            return "login";
        }
    }

}
