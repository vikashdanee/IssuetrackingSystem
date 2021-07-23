package com.system.issuetracking.user.controller;

import com.system.issuetracking.entity.EmailTemplate;
import com.system.issuetracking.user.model.User;
import com.system.issuetracking.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Random;

/**
 * @author Monica Rajbhandari on 7/8/21
 */

@Controller
@AllArgsConstructor
@SuppressWarnings("Duplicates")
public class LoginController {

    @Autowired
    private final UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String getLogin(ModelMap map) {
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, ModelMap map) {
        User newUser  = userService.checkUserExist(user);
        if (null != newUser) {
            return "dashboard";
        }else {
            map.put("msg", "Invalid email or password!");
            return "login";
        }
    }

    @GetMapping("/reset_password")
    public String getForgotPage(ModelMap map) {
        return "forgot-password";
    }

    @PostMapping("/reset_password")
    public String resetPassword(@ModelAttribute User user, ModelMap map) throws MessagingException {
        User newUser  = userService.getUserByUsername(user.getEmail());
        if (null != newUser) {
            Random random = new Random();
            String rawPassword = String.valueOf(random.nextInt(100000));
            newUser.setPassword(rawPassword);
            userService.save(newUser);
            sendNotification(user,rawPassword);
            map.put("msg", "Temporary password is send to the entered email address!");
            return "forgot-password";
        }else {
            map.put("msg", "Email address does not exist!");
            return "forgot-password";
        }
    }

    public void sendNotification(User user,String password) throws MessagingException {
        String subject="Password Reset Request";
        String body="<p>Please, use following temporary password for login.</p>"
                +"<p>"+password+"</p>"
                +"<p> Note: Do not forgot to change password once logged in!</p>";
        EmailTemplate emailTemplate = new EmailTemplate(user.getFirstName(),body,"");

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(emailTemplate.returnContent(), true);
        javaMailSender.send(msg);
    }


}
