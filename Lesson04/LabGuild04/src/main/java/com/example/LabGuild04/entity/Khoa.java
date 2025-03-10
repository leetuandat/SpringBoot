/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: LabGuild04
 * @date: 3/5/2025
 * @time: 05:13 PM
 * @package: com.example.LabGuild04.entity
 */

package com.example.LabGuild04.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.Generated;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "khoa")
public class Khoa {
    @Id
    @Column(length = 10)
    private String makh;

    @Column(nullable = false, length = 100)
    private String tenkh;
}
