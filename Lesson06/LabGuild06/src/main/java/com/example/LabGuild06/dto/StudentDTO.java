/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild06
 * @date: 3/11/2025
 * @time: 09:00 PM
 * @package: com.example.LabGuild06.dto
 */

package com.example.LabGuild06.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StudentDTO {
    private Long id;
    private String name;
    private String email;
    private int age;
}
