/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/14/2025
 * @time: 04:40 PM
 * @package: com.example.Project.auth
 */

package com.example.Project.service.impl;

import com.example.Project.config.OtpProperties;
import com.example.Project.auth.OtpPurpose;
import com.example.Project.auth.OtpToken;
import com.example.Project.auth.OtpTokenRepository;
import com.example.Project.repository.CustomerRepository;
import com.example.Project.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final OtpTokenRepository otpRepo;
    private final CustomerRepository customerRepo;
//    private final CustomerService customerService;
    private final MailService mailService;
    private final OtpProperties props;

    private final SecureRandom rnd = new SecureRandom();
    private static final ZoneId ZONE_HANOI = ZoneId.of("Asia/Bangkok");
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy").withZone(ZONE_HANOI);

    private String genCode(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) sb.append(rnd.nextInt(10));
        return sb.toString();
    }

    private String fmtLocal(Instant t) {
        return DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy")
                .withZone(ZONE_HANOI).format(t);
    }


/** Xác minh OTP cho REGISTER, kích hoạt tài khoản. */
    @Transactional
    public void verifyRegisterOtp(String email, String code) {
        var token = otpRepo.findTopByEmailAndPurposeAndUsedIsFalseOrderByCreatedAtDesc(
                        email, OtpPurpose.REGISTER)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy OTP. Vui lòng yêu cầu mã mới."));
        if (Instant.now().isAfter(token.getExpiresAt())) {
            throw new IllegalStateException("OTP đã hết hạn. Vui lòng yêu cầu mã mới.");
        }
        if (!token.getCode().equals(code)) {
            token.setAttempts(Math.max(0, token.getAttempts() - 1));
            if (token.getAttempts() <= 0) token.setUsed(true);
            otpRepo.save(token);
            throw new IllegalArgumentException("Mã OTP không đúng. Vui lòng thử lại.");
        }
        token.setUsed(true);
        otpRepo.save(token);

        // chỉ cập nhật emailVerified, không đụng isActive
        var customer = customerRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy tài khoản để kích hoạt."));
        customer.setEmailVerified(true);
        customerRepo.save(customer);
    }

    /** Gửi lại OTP (không tạo customer mới). */
    @Transactional
    public void resendRegisterOtp(String email) {
        var customer = customerRepo.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new IllegalStateException("Email chưa đăng ký."));
        if (Boolean.TRUE.equals(customer.isEmailVerified())) {
            throw new IllegalStateException("Email đã xác thực, không cần gửi lại OTP.");
        }
        issueRegisterOtpForExistingCustomer(customer.getId(), customer.getEmail());
    }

    @Transactional
    public void issueRegisterOtpForExistingCustomer(Long customerId, String email) {
        var oneHourAgo = Instant.now().minus(Duration.ofHours(1));
        if (otpRepo.countByEmailAndPurposeAndCreatedAtAfter(email, OtpPurpose.REGISTER, oneHourAgo)
                >= props.getMaxPerHour()) {
            throw new IllegalStateException("Bạn đã yêu cầu OTP quá nhiều lần. Vui lòng thử lại sau.");
        }
        var last = otpRepo.findTopByEmailAndPurposeAndUsedIsFalseOrderByCreatedAtDesc(
                email, OtpPurpose.REGISTER).orElse(null);
        if (last != null && last.getCreatedAt().isAfter(Instant.now()
                .minusSeconds(props.getResendCooldownSeconds()))) {
            throw new IllegalStateException("Vui lòng đợi thêm trước khi yêu cầu gửi lại OTP.");
        }

        var code = genCode(props.getCodeLength());
        var token = OtpToken.builder()
                .customerId(customerId)
                .email(email)
                .purpose(OtpPurpose.REGISTER)
                .code(code)
                .expiresAt(Instant.now().plus(Duration.ofMinutes(props.getTtlMinutes())))
                .attempts(5)
                .used(false)
                .build();
        otpRepo.save(token);

        mailService.sendOtp(email, props.getBrand(), code, FMT.format(token.getExpiresAt()));
    }

}
