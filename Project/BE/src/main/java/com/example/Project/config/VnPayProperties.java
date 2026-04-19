/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/21/2025
 * @time: 02:30 AM
 * @package: com.example.Project.config
 */

package com.example.Project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vnpay")
@Getter
@Setter
public class VnPayProperties {
    private String tmnCode;
    private String hashSecret;
    private String payUrl;
    private String version;
    private String command;
    private String currCode;
    private String locale;
    private String orderType;
    private String returnUrl;
    private String ipnUrl;
    private boolean testMode;
}
