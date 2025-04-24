/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 4/23/2025
 * @time: 05:53 PM
 * @package: com.example.Project.entity
 */

package com.example.Project.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AUTHOR")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 250, nullable = false)
    private String name;

    @Column(length = 100)
    private String authorKey; // ví dụ: /authors/OL1234567A

    @Lob
    private String biography;

    @Column(length = 500)
    private String image; // URL ảnh tác giả

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> books;

}
