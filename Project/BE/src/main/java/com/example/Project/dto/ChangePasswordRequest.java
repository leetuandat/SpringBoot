/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 5/22/2025
 * @time: 05:34 PM
 * @package: com.example.Project.dto
 */

package com.example.Project.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
