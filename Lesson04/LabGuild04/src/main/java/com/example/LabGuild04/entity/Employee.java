/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/5/2025
 * @time: 05:24 PM
 * @package: com.example.LabGuild04.entity
 */

package com.example.LabGuild04.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private Long id;

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false)
    private String gender;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private double Salary;

}
