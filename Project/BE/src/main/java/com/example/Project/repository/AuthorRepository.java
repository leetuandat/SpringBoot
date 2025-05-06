package com.example.Project.repository;

import com.example.Project.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    List<Author> findByNameIgnoreCaseContaining(String name);
}
