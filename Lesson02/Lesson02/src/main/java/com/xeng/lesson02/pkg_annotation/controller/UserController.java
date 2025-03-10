/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: SpringBoot
 * @date: 2/25/2025
 * @time: 06:00 PM
 * @package: com.xeng.lesson02.pkg_annotation.controller
 */

package com.xeng.lesson02.pkg_annotation.controller;

import com.xeng.lesson02.pkg_annotation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user")

    @GetMapping()
    public String getUsers() {
        return userService.getUserDetails();
    }

    @PostMapping()
    public String createUser() {
        return "<h1>User created";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable int id) {
        return "<h1>User with ID " + id + " updated";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable int id) {
        return "<h1>User with ID " + id + " deleted";
    }
}
