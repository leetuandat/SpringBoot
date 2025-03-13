/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson06
 * @date: 3/11/2025
 * @time: 07:14 PM
 * @package: com.example.Lesson06.service
 */

package com.example.Lesson06.service;

import com.example.Lesson06.entity.User;
import com.example.Lesson06.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
