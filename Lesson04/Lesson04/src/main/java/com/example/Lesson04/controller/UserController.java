/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson04
 * @date: 2/28/2025
 * @time: 07:31 PM
 * @package: com.example.Lesson04.controller
 */

package com.example.Lesson04.controller;

import com.example.Lesson04.dto.UserDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody @Valid UserDTO userDTO) {
        return ResponseEntity.ok("User created successfully");
    }
}
