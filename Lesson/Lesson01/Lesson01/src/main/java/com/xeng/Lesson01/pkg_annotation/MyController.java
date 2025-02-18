/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson01
 * @date: 2/18/2025
 * @time: 08:38 PM
 * @package: com.xeng.Lesson01.pkg_annotation
 */

package com.xeng.Lesson01.pkg_annotation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @GetMapping("/hello")
    public String hello() {
        return "hello world";
    }
}
