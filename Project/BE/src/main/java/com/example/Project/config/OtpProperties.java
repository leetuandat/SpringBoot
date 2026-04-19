/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/14/2025
 * @time: 04:37 PM
 * @package: com.example.Project.auth
 */

package com.example.Project.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "otp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OtpProperties {
    int codeLength;
    int ttlMinutes;
    int resendCooldownSeconds;
    int maxPerHour;
    String brand;
}
