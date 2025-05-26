package com.example.Project.repository;

import com.example.Project.entity.Category;
import com.example.Project.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategory_NameIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findAllByIsDeleteFalse(Pageable pageable);
    Page<Product> findByNameContainingIgnoreCaseAndIsDeleteFalse(String keyword, Pageable pageable);
    Page<Product> findByCategory_NameIgnoreCaseAndIsDeleteFalse(String keyword, Pageable pageable);

}
