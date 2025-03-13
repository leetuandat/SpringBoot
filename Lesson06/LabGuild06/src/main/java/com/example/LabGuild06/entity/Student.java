/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild06
 * @date: 3/11/2025
 * @time: 08:57 PM
 * @package: com.example.LabGuild06.entity
 */

package com.example.LabGuild06.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private int age;
}
