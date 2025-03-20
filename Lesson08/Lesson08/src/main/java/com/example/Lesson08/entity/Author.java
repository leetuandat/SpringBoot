/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Lesson08
 * @date: 3/18/2025
 * @time: 06:54 PM
 * @package: com.example.Lesson08.entity
 */

package com.example.Lesson08.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String code;
    String name;
    String description;
    String imgUrl;
    String email;
    String phone;
    String address;
    Boolean isActive;

    //Tạo mqh với bảng book
    @ManyToMany(mappedBy = "authors")
    private List<Book> books = new ArrayList<>();
}
