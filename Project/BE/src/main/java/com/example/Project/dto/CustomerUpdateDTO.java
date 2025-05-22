/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/21/2025
 * @time: 04:00 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

public record CustomerUpdateDTO(
        String name,
        String email,
        String phone,
        String address,
        String avatar
) {}

