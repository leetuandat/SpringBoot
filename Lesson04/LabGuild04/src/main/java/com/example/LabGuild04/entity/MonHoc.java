/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/5/2025
 * @time: 05:20 PM
 * @package: com.example.LabGuild04.entity
 */

package com.example.LabGuild04.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "monhoc")
public class MonHoc {
    @Id
    @Column(length = 10)
    private String mamh;

    @Column(nullable = false, length = 100)
    private String tenmh;

    @Column(nullable = false)
    private int sotiet;
}
