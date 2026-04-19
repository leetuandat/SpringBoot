package com.example.Project.repository;

import com.example.Project.entity.PaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Page<PaymentMethod> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // ĐÚNG: dùng IsDelete (khớp với field 'isDelete')
    Optional<PaymentMethod> findByNameAndIsDeleteFalse(String name);

    // ĐÚNG: bạn đã viết đúng method này
    List<PaymentMethod> findAllByIsActiveTrueAndIsDeleteFalseOrderByNameAsc();
}
