/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild05
 * @date: 3/11/2025
 * @time: 04:50 PM
 * @package: com.example.LabGuild05.controller
 */

package com.example.LabGuild05.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class UserController {

    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("title", "Trang chủ");
        model.addAttribute("content", "home :: content");
        return "layout";
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {
        model.addAttribute("title", "Trang quản trị");
        return "admin";
    }

    @GetMapping("/list")
    public String listPage(Model model) {
        model.addAttribute("title", "Danh sách");
        return "list";
    }
}
