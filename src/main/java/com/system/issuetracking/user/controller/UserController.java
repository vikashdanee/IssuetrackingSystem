package com.system.issuetracking.user.controller;

import com.system.issuetracking.enums.UserType;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Monica Rajbhandari on 7/9/21
 */

@Controller
@RequestMapping(UserController.URI)
@AllArgsConstructor
public class UserController {

    public static final String URI = "/users";
    private static final String WARN_MESSAGE = "User Registration Failed!";
    private static final String SUCCESS_MESSAGE = "User has been successfully Register!";

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String getUserRegisterPage() {
        return "user";
    }

    @GetMapping
    public String getUserPage(ModelMap map) {
        List<User> users = userService.findAll();
        map.put("users", users);
        return "users";
    }

    @PostMapping("/user")
    private String getSaveUser(@ModelAttribute User user, ModelMap map){
        user = userService.save(user);
        if(user !=null){
            map.put("msg",SUCCESS_MESSAGE);
            return "redirect:/users";
        }
        else {
            map.put("msg",WARN_MESSAGE);
            return "user";
        }
    }

    @GetMapping("/{action}/{id}")
    public String deleteById(@PathVariable String action, @PathVariable Long id, ModelMap map) {
        switch (action) {
            case "delete":
                userService.deleteById(id);
                return "redirect:/users";

            case "edit":
                User user = userService.findById(id);
                map.put("user", user);
                return "user";
            default:
                return "redirect:/users";
        }

    }
}
