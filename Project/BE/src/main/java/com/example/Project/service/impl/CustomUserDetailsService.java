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
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
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
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        // Cho phép login bằng username hoặc email
        Customer c = customerRepository.findByUsername(loginId)
                .orElseGet(() -> customerRepository.findByEmailIgnoreCase(loginId)
                        .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản")));

        // Tính toán các cờ theo Cách B + soft delete
        boolean enabled = Boolean.TRUE.equals(c.isEmailVerified())
                && Boolean.TRUE.equals(c.getIsActive())
                && !Boolean.TRUE.equals(c.getIsDelete());

        boolean accountNonLocked = !Boolean.TRUE.equals(c.getIsDelete()); // xoá mềm coi như lock
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;

        List<GrantedAuthority> authorities = List.of(() -> c.getRole()); // "ROLE_USER", ...

        return new UserPrincipal(
                c.getId(),
                c.getUsername(),           // hoặc dùng c.getEmail() làm principal tuỳ bạn
                c.getPassword(),
                List.of(() -> c.getRole()),
                enabled,
                accountNonLocked,
                accountNonExpired,
                credentialsNonExpired
        );
    }
}
