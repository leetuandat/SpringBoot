package com.example.Project.repository;


import com.example.Project.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);
    @Query("""
  select c from Coupon c
  where c.isActive = true and c.isDelete = false
    and c.startAtUtc <= :now and c.endAtUtc > :now
""")
    List<Coupon> findAllActiveNow(@Param("now") LocalDateTime now);
}

