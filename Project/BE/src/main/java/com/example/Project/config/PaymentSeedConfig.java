/**
 *
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/21/2025
 * @time: 02:31 AM
 * @package: com.example.Project.config
 */

package com.example.Project.config;

import com.example.Project.entity.PaymentMethod;
import com.example.Project.repository.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"dev","local"})
@RequiredArgsConstructor
public class PaymentSeedConfig {

    private final PaymentMethodRepository repo;

    @Bean
    @Profile({"dev","local"}) // bật profile dev/local để seed
    CommandLineRunner seedPaymentMethods() {
        return args -> {
            upsert("COD", "Thanh toán khi nhận hàng");
            upsert("BANK_TRANSFER", "Chuyển khoản ngân hàng");
            upsert("VNPAY", "Cổng thanh toán VNPay sandbox");
        };
    }

    private void upsert(String name, String notes) {
        repo.findByNameAndIsDeleteFalse(name).orElseGet(() -> {
            PaymentMethod pm = new PaymentMethod();
            pm.setName(name);
            pm.setNotes(notes);
            pm.setIsActive(true);
            pm.setIsDelete(false);
            return repo.save(pm);
        });
    }
}
