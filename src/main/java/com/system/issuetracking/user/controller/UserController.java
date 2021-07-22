package com.system.issuetracking.user.controller;

import com.system.issuetracking.enums.UserType;
import com.system.issuetracking.user.model.Role;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;


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
    public String getUserRegisterPage(@ModelAttribute User user,Model model) {
        List<Role> listRoles = userService.findAllRole();
        model.addAttribute("listRoles", listRoles);
        return "user";
    }

    @GetMapping
    public String getUserPage(ModelMap map) {
        List<User> users = userService.findAll();
        map.put("users", users);
        return "users";
    }

    @PostMapping("/user")
    private String getSaveUser(@ModelAttribute User user, ModelMap map) throws MessagingException {
        /* check if userrole is null */
        if(user.getUserRole()==null)
            user.setUserRole("User");
        Role roleUser = userService.findRoleByName(user.getUserRole());
        user.addRole(roleUser);
        user = userService.saveNewUser(user);
        if(user !=null){
            map.put("msg",SUCCESS_MESSAGE);
            return "redirect:/users";
        }
        else {
            map.put("msg",WARN_MESSAGE);
            return "user";
        }
    }

    @PostMapping("/update-user/{id}")
    private String getSaveUser(@PathVariable Long id,@ModelAttribute User user, ModelMap map){
        User existedUser = userService.findById(id);
        if(existedUser==null){
            existedUser = new User();
        }
        existedUser.setFirstName(user.getFirstName());
        existedUser.setLastName(user.getLastName());
        existedUser.setDesignation(user.getDesignation());
        existedUser.setEmail(user.getEmail());
        existedUser.setPassword(user.getPassword());
        existedUser.setUserRole(user.getUserRole());
        existedUser.setRoles(new HashSet<>());
        /* check if userrole is null */
        if(existedUser.getUserRole()==null)
            existedUser.setUserRole("User");
        Role roleUser = userService.findRoleByName(existedUser.getUserRole());
        existedUser.addRole(roleUser);
        user = userService.save(existedUser);
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
    public String deleteById(@PathVariable String action, @PathVariable Long id, Model model) {
        switch (action) {
            case "delete":
                userService.deleteById(id);
                return "redirect:/users";

            case "edit":
                List<Role> listRoles = userService.findAllRole();
                model.addAttribute("listRoles", listRoles);
                User user = userService.findById(id);
                model.addAttribute("user", user);
                return "updateUser";
            default:
                return "redirect:/users";
        }

    }
}
