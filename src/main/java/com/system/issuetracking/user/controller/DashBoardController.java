package com.system.issuetracking.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Monica Rajbhandari on 7/9/21
 */

@Controller
@RequestMapping("/dashboard")
@AllArgsConstructor
public class DashBoardController {

    @GetMapping
    public String getDashBoard(ModelMap map) {
        return "dashboard";
    }
}
