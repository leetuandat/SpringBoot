/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/17/2025
 * @time: 07:54 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.auth.dto.OtpVerifyRequest;
import com.example.Project.auth.dto.ResendOtpRequest;
import com.example.Project.dto.CustomerDTO;
import com.example.Project.entity.Customer;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.service.impl.OtpService;
import jakarta.validation.Valid;
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
    private final OtpService otpService;


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid CustomerDTO dto) {
        // 1) Check username trùng
        if (customerRepository.findByUsername(dto.username()).isPresent()) {
            return ResponseEntity.badRequest().body("Tài khoản (username) đã tồn tại");
        }

        // 2) Check email trùng
        var existingByEmail = customerRepository.findByEmailIgnoreCase(dto.email()).orElse(null);
        if (existingByEmail != null) {
            if (Boolean.TRUE.equals(existingByEmail.isEmailVerified())) {
                return ResponseEntity.badRequest().body("Email đã tồn tại");
            } else {
                // email đã đăng ký nhưng CHƯA verify -> gửi lại OTP
                otpService.resendRegisterOtp(existingByEmail.getEmail());
                return ResponseEntity.ok("Email đã đăng ký nhưng chưa xác thực. Đã gửi lại mã OTP.");
            }
        }

        // 3) Tạo user mới: emailVerified=false, isActive=true
        Customer customer = new Customer();
        customer.setUsername(dto.username());
        customer.setPassword(passwordEncoder.encode(dto.password()));
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setPhone(dto.phone());
        customer.setAddress(dto.address());
        customer.setAvatar(dto.avatar());
        customer.setIsDelete(false);
        customer.setIsActive(true);           // Cách B: vẫn active, login sẽ kiểm emailVerified
        customer.setEmailVerified(false);     // CHƯA xác thực email
        customer.setRole("ROLE_USER");

        Customer saved = customerRepository.save(customer);

        // 4) Gửi OTP
        otpService.issueRegisterOtpForExistingCustomer(saved.getId(), saved.getEmail());

        return ResponseEntity.ok("Đăng ký thành công. Vui lòng kiểm tra email để nhập mã OTP.");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody @Valid OtpVerifyRequest req) {
        otpService.verifyRegisterOtp(req.getEmail(), req.getCode());
        return ResponseEntity.ok("Xác thực email thành công. Bạn có thể đăng nhập.");
    }

    @PostMapping("/otp/resend")
    public ResponseEntity<?> resendOtp(@RequestBody @Valid ResendOtpRequest req) {
        otpService.resendRegisterOtp(req.getEmail());
        return ResponseEntity.ok("Đã gửi lại mã OTP.");
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
