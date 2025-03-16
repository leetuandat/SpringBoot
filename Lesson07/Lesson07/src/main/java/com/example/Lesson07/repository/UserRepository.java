package com.example.Lesson07.repository;

import com.example.Lesson07.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //Kiểm tra username trùng
    boolean existsByUserName(String userName);
}
