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
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CustomerRepository CustomerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Customer customer = CustomerRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Không tìm thầy tài khoản"));

        return User.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
                .roles(customer.getRole().replace("ROLE_", ""))
                .build();
    }
}
