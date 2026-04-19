/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/14/2025
 * @time: 04:10 PM
 * @package: com.example.Project.auth
 */

package com.example.Project.auth;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "otp_tokens",
    indexes = {
            @Index(name = "idx_otp_email_purpose", columnList = "email,purpose"),
            @Index(name = "idx_otp_created_at", columnList = "createdAt")
    })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    Long customerId;
    @Column(nullable = false) String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) OtpPurpose purpose;

    @Column(nullable = false) String code;

    @Column(nullable = false) private Instant expiresAt;
    @Column(nullable = false) private Integer attempts;
    @Column(nullable = false) private Boolean used;

    @Column(nullable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;

    @PrePersist void prePersist(){ createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate  void preUpdate(){ updatedAt = Instant.now(); }
}
