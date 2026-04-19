/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/19/2025
 * @time: 02:35 PM
 * @package: com.example.Project.service.impl
 */

package com.example.Project.service.impl;

import com.example.Project.dto.coupon.CouponResponse;
import com.example.Project.dto.coupon.CreateCouponRequest;
import com.example.Project.entity.Category;
import com.example.Project.entity.Coupon;
import com.example.Project.entity.PaymentMethod;
import com.example.Project.entity.Product;
import com.example.Project.repository.CategoryRepository;
import com.example.Project.repository.CouponRepository;
import com.example.Project.repository.PaymentMethodRepository;
import com.example.Project.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepo;
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final PaymentMethodRepository paymentMethodRepo;

    @Transactional
    public CouponResponse create(CreateCouponRequest r) {
        // Unique code check (nếu SINGLE)
        if (r.getCodeType() == Coupon.CodeType.SINGLE && r.getCode() != null) {
            if (couponRepo.existsByCodeIgnoreCase(r.getCode())) {
                throw new IllegalArgumentException("Mã coupon đã tồn tại: " + r.getCode());
            }
        }

        var coupon = Coupon.builder()
                .codeType(r.getCodeType())
                .code(r.getCode())
                .type(r.getType())
                .scope(Coupon.CouponScope.PLATFORM) // <-- ép PLATFORM
                .shopId(null)                       // <-- luôn null
                .startAtUtc(r.getStartAtUtc())
                .endAtUtc(r.getEndAtUtc())
                .description(r.getDescription())
                .discountValue(r.getDiscountValue())
                .maxDiscountValue(r.getMaxDiscountValue())
                .minOrderAmount(r.getMinOrderAmount())
                .usageLimitTotal(r.getUsageLimitTotal())
                .usageLimitPerUser(r.getUsageLimitPerUser())
                .budgetCap(r.getBudgetCap())
                .newUserOnly(Boolean.TRUE.equals(r.getNewUserOnly()))
                .firstOrderOnly(Boolean.TRUE.equals(r.getFirstOrderOnly()))
                .isActive(Boolean.TRUE.equals(r.getIsActive()))
                .build();

        // Gắn quan hệ nếu có
        if (r.getProductIds() != null && !r.getProductIds().isEmpty()) {
            var products = new HashSet<Product>(productRepo.findAllById(r.getProductIds()));
            coupon.setProducts(products);
        }
        if (r.getCategoryIds() != null && !r.getCategoryIds().isEmpty()) {
            var categories = new HashSet<Category>(categoryRepo.findAllById(r.getCategoryIds()));
            coupon.setCategories(categories);
        }
        if (r.getPaymentMethodIds() != null && !r.getPaymentMethodIds().isEmpty()) {
            var pms = new HashSet<PaymentMethod>(paymentMethodRepo.findAllById(r.getPaymentMethodIds()));
            coupon.setPaymentMethods(pms);
        }

        // @PrePersist/@PreUpdate sẽ tự validate rule
        var saved = couponRepo.save(coupon);
        return new CouponResponse(saved.getId(), saved.getCode());
    }

    @Transactional
    public CouponResponse update(Long id, CreateCouponRequest r) {
        var coupon = couponRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Coupon không tồn tại: id=" + id));

        // ===== Normalize & validate code theo CodeType =====
        var newCodeType = r.getCodeType();
        String newCode = r.getCode();
        if (newCode != null) newCode = newCode.trim();

        if (newCodeType == Coupon.CodeType.SINGLE) {
            if (newCode == null || newCode.isBlank()) {
                throw new IllegalArgumentException("CodeType=SINGLE yêu cầu 'code'.");
            }
            newCode = newCode.toUpperCase();

            // Chỉ check trùng khi code thay đổi thực sự
            String oldCode = coupon.getCode();
            boolean codeChanged = (oldCode == null) || !oldCode.equalsIgnoreCase(newCode);
            if (codeChanged && couponRepo.existsByCodeIgnoreCase(newCode)) {
                throw new IllegalArgumentException("Mã coupon đã tồn tại: " + newCode);
            }
            coupon.setCode(newCode);
        } else {
            // UNIQUE_POOL / AUTO_APPLY: không dùng code chung
            coupon.setCode(null);
        }
        coupon.setCodeType(newCodeType);

        // ===== Thuộc tính chính =====
        coupon.setType(r.getType());

        // Single-store: luôn PLATFORM
        coupon.setScope(Coupon.CouponScope.PLATFORM);
        coupon.setShopId(null);

        coupon.setStartAtUtc(r.getStartAtUtc());
        coupon.setEndAtUtc(r.getEndAtUtc());

        coupon.setDescription(r.getDescription());
        coupon.setDiscountValue(r.getDiscountValue());
        coupon.setMaxDiscountValue(r.getMaxDiscountValue());
        coupon.setMinOrderAmount(r.getMinOrderAmount());

        coupon.setUsageLimitTotal(r.getUsageLimitTotal());
        coupon.setUsageLimitPerUser(r.getUsageLimitPerUser());
        coupon.setBudgetCap(r.getBudgetCap());

        coupon.setNewUserOnly(Boolean.TRUE.equals(r.getNewUserOnly()));
        coupon.setFirstOrderOnly(Boolean.TRUE.equals(r.getFirstOrderOnly()));
        coupon.setIsActive(Boolean.TRUE.equals(r.getIsActive()));

        // ===== Quan hệ (nếu trường request != null thì cập nhật; =null thì giữ nguyên) =====
        if (r.getProductIds() != null) {
            var products = new java.util.HashSet<Product>(productRepo.findAllById(r.getProductIds()));
            coupon.setProducts(products);
        }
        if (r.getCategoryIds() != null) {
            var categories = new java.util.HashSet<Category>(categoryRepo.findAllById(r.getCategoryIds()));
            coupon.setCategories(categories);
        }
        if (r.getPaymentMethodIds() != null) {
            var pms = new java.util.HashSet<PaymentMethod>(paymentMethodRepo.findAllById(r.getPaymentMethodIds()));
            coupon.setPaymentMethods(pms);
        }

        // @PreUpdate trong entity sẽ tự validate thêm các rule (thời gian, value, ...)
        var saved = couponRepo.save(coupon);
        return new CouponResponse(saved.getId(), saved.getCode());
    }

}
