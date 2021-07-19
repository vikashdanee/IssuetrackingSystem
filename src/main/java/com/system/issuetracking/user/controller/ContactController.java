package com.system.issuetracking.user.controller;

import com.system.issuetracking.entity.EmailTemplate;
import com.system.issuetracking.issue.model.Issue;
import com.system.issuetracking.user.model.Message;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * @author Monica Rajbhandari on 7/8/21
 */

@Controller
@AllArgsConstructor
public class ContactController {

    @Autowired
    private JavaMailSender javaMailSender;

    @GetMapping("/contact")
    public String getContactPage() {
        return "contact";
    }

    @GetMapping("/services")
    public String getServicesPage() {
        return "services";
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "about";
    }

    @GetMapping("/contact/send")
    public String getSentServices(@ModelAttribute Message message, ModelMap map) throws MessagingException {

        String body="<p>Following message has been received from "+message.getName()+" ("+message.getEmail()+")</p>"
                +"<p>"+message.getBody()+"</p>";

        EmailTemplate emailTemplate = new EmailTemplate("",body,"");

        MimeMessage msg = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo("issuemanagement22@gmail.com");
        helper.setSubject(message.getSubject());
        helper.setText(emailTemplate.returnContent(), true);
        javaMailSender.send(msg);

        map.put("msg","Message successfully sent!");

        return "contact";
    }
}
