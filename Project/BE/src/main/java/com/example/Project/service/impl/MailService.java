/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/14/2025
 * @time: 04:32 PM
 * @package: com.example.Project.mail
 */

package com.example.Project.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    public void sendOtp(String to, String brand, String code, String expireAtLocal) {
        var msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject("["+ brand +"] Mã xác thực tài khoản");
        msg.setText("""
                Xin chào, 
                
                Mã OTP của bạn là: %s
                Mã sẽ hết hạn lúc: %s
                
                Nếu bạn không yêu cầu, hãy bỏ qua email này.
                
                Trân trọng, 
                %s
                """.formatted(code, expireAtLocal, brand));
        mailSender.send(msg);
    }
}
