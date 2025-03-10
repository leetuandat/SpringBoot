/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: SpringBoot
 * @date: 2/25/2025
 * @time: 06:27 PM
 * @package: com.xeng.lesson02.pkg_annotation.service
 */

package com.xeng.lesson02.pkg_annotation.service;

import org.springframework.stereotype.Service;

@Service
public class MyGreetingService {
    public String greet() {
        return "<h1>Hello from MyGreetingService!";
    }
}
