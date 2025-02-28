/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: SpringBoot
 * @date: 2/25/2025
 * @time: 05:21 PM
 * @package: com.xeng.lesson02.ioc_spring
 */

package com.xeng.lesson02.ioc_spring;

import org.springframework.stereotype.Service;

@Service
public class GreetingServiceImpl implements GreetingService {
    @Override
    public String greet(String name) {
        return "<h2> Devmaster[Spring Boot!] Xin chào, <h2>" +
                "<h1 style='color:red; text-align:center;>" + name;
    }
}
