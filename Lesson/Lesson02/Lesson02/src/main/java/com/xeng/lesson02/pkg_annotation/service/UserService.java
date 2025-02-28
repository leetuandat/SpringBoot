/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: SpringBoot
 * @date: 2/25/2025
 * @time: 06:16 PM
 * @package: com.xeng.lesson02.pkg_annotation.service
 */

package com.xeng.lesson02.pkg_annotation.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public String getUserDetails() {
        return "<h1>User details";
    }
}
