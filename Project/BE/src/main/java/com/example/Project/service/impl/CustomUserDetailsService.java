/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/17/2025
 * @time: 07:41 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.entity.Customer;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản"));

        // Tạo UserPrincipal trả về, chứa id của customer
        return new UserPrincipal(
                customer.getId(),
                customer.getUsername(),
                customer.getPassword(),
                // chuyển đổi role thành GrantedAuthority list, ví dụ:
                List.of(() -> customer.getRole())  // hoặc map roles sang SimpleGrantedAuthority
        );
    }
}
