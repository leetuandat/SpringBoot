package com.example.Project.auth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
    Optional<OtpToken> findTopByEmailAndPurposeAndUsedIsFalseOrderByCreatedAtDesc(String email, OtpPurpose purpose);
    int countByEmailAndPurposeAndCreatedAtAfter(String email, OtpPurpose purpose, Instant after);
}
