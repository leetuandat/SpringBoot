/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: Project
 * @date: 4/9/2025
 * @time: 02:51 AM
 * @package: com.example.Project.repository
 */

package com.example.Project.repository;

import com.example.Project.entity.NewsCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {
    Page<NewsCategory> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
