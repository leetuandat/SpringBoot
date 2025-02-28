/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: SpringBoot
 * @date: 2/25/2025
 * @time: 06:28 PM
 * @package: com.xeng.lesson02.pkg_annotation.controller
 */

package com.xeng.lesson02.pkg_annotation.controller;

import com.xeng.lesson02.pkg_annotation.service.MyGreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyGreetingController {
    @Autowired
    private final MyGreetingService myGreetingService;
    public MyGreetingController(MyGreetingService greetingService) {
        this.myGreetingService = greetingService;
    }

    @GetMapping("/my-greet")
    public String greet() {
        return myGreetingService.greet();
    }
}
