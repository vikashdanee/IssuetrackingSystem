package com.system.issuetracking.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Monica Rajbhandari on 7/8/21
 */

@Controller
@AllArgsConstructor
public class ContactController {

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
}
