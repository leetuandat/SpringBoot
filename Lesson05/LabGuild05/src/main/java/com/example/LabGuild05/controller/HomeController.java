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


@Controller
public class HomeController {
    @GetMapping("/home")
    public String homePage(Model model) {
        model.addAttribute("title", "Trang chủ");
        model.addAttribute("content", "home :: content"); // Đảm bảo fragment đúng
        return "layout";
    }
}

