/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: SpringBoot
 * @date: 2/25/2025
 * @time: 06:33 PM
 * @package: com.xeng.lesson02.pkg_annotation.controller
 */

package com.xeng.lesson02.pkg_annotation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyParamController {
    //Sử dụng @RequestParam để lấy query parameter từ URL

    @GetMapping("/my-param")
    public String searchUser(@RequestParam (value = "name", required = false) String name) {
        if (name == null) {
            return "<h2>No name provided, returning all users";
        }
        return "<h2>Searching for users with name: " + name;
    }

    @GetMapping("/my-variable/{id}")
    public String getUserById(@PathVariable(required = false) String id) {
        return "<h1>User ID is " + id;
    }
}
