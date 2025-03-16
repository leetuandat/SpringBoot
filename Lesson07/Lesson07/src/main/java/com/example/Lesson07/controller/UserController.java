/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson07
 * @date: 3/14/2025
 * @time: 07:24 PM
 * @package: com.example.Lesson07.controller
 */

package com.example.Lesson07.controller;

import com.example.Lesson07.entity.User;
import com.example.Lesson07.repository.UserRepository;
import com.example.Lesson07.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "users/list";
    }

    @GetMapping("/new")
    public String createUserForm(Model model) {
        User user = new User();
        user.setIsActive(true);
        model.addAttribute("user", user);
        return "users/form";
    }

    @PostMapping("/save")
    public String saveUser(@Validated @ModelAttribute("user") User user, BindingResult result, Model model) {
        if(userRepository.existsByUserName(user.getUserName()) && user.getId() == null) {
            result.rejectValue("userName", "error.user", "UserName đã tồn tại, vui lòng chọn tên khác.");
            return "users/form";
        }
        if(result.hasErrors()) {
            return "users/form";
        }
        userService.saveUser(user);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user id: "+id));
        model.addAttribute("user", user);
        return "users/form";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
