/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/17/2025
 * @time: 07:54 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.CustomerDTO;
import com.example.Project.entity.Customer;
import com.example.Project.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public String register(@RequestBody CustomerDTO dto) {
        if(customerRepository.findByUsername(dto.username()).isPresent()) {
            return "Tài khoản đã tồn tại";
        }

        Customer customer = new Customer();
        customer.setUsername(dto.username());
        customer.setPassword(passwordEncoder.encode(dto.password()));
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setAddress(dto.address());
        customer.setAvatar(dto.avatar());
        customer.setIsDelete(false);
        customer.setIsActive(dto.isActive() != null ? dto.isActive() : true);
        customer.setRole("ROLE_USER");
        customerRepository.save(customer);

        return "Đăng ký thành công";
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).body("Not logged in");
        Customer customer = customerRepository.findByUsername(userDetails.getUsername()).orElseThrow();
        return ResponseEntity.ok(Map.of(
                "id", customer.getId(),
                "username", customer.getUsername(),
                "name", customer.getName(),
                "email", customer.getEmail(),
                "phone", customer.getPhone(),
                "address", customer.getAddress(),
                "avatar", customer.getAvatar(),
                "role", customer.getRole()
        ));

    }

}
