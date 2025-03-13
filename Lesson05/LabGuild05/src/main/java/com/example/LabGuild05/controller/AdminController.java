/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild05
 * @date: 3/11/2025
 * @time: 05:29 PM
 * @package: com.example.LabGuild05.controller
 */

package com.example.LabGuild05.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class AdminController {

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("title", "Trang Quản Trị");
        model.addAttribute("menuItems", Arrays.asList("Dashboard", "Posts", "Users", "Settings"));
        model.addAttribute("content", "admin :: content");
        return "admin-layout";
    }
}
