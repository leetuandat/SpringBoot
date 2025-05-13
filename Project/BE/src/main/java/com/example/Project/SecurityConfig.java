/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/9/2025
 * @time: 05:52 PM
 * @package: com.example.Project
 */

package com.example.Project;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> request
                        .anyRequest().permitAll()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}


