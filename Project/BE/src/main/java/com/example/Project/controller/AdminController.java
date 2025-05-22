/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/17/2025
 * @time: 08:00 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @GetMapping("/hello")
    public String helloAdmin() {
        return "Hello ADMIN quyền lực!";
    }

}
