/**
 * @author X.e.n.g
 * @version 1.O
 * @project name: BE
 * @date: 8/19/2025
 * @time: 02:37 PM
 * @package: com.example.Project.controller
 */

package com.example.Project.controller;

import com.example.Project.dto.OrderDTO;
import com.example.Project.dto.coupon.CouponResponse;
import com.example.Project.dto.coupon.CreateCouponRequest;
import com.example.Project.entity.Coupon;
import com.example.Project.entity.Coupon.CouponScope;
import com.example.Project.entity.Coupon.CouponType;
import com.example.Project.entity.Coupon.CodeType;
import com.example.Project.entity.TransportMethod;
import com.example.Project.repository.CouponRepository;
import com.example.Project.repository.TransportMethodRepository;
import com.example.Project.service.CartService;
import com.example.Project.service.impl.CouponService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import static java.math.BigDecimal.ZERO;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
@Validated
public class CouponController {

    private final CouponService couponService;                 // create/update dùng service của bạn
    private final CouponRepository couponRepository;           // các thao tác đọc khác
    private final CartService cartService;                     // để lấy giỏ hiện tại
    private final TransportMethodRepository transportRepo;     // để ước tính phí ship

    // =========================================================
    //                        ADMIN CRUD
    // =========================================================

    @GetMapping
    public Page<Coupon> listCoupons(
            @RequestParam(required = false) CouponType type,
            @RequestParam(required = false) CodeType codeType,
            @RequestParam(required = false) Boolean active,
            @RequestParam(required = false) String q,
            Pageable pageable
    ) {
        // TODO: nếu bạn có Specification, thay thế bằng finder có filter (type/codeType/active/q)
        return couponRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Coupon getCoupon(@PathVariable Long id) {
        return couponRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<CouponResponse> create(
            @Valid @RequestBody CreateCouponRequest req,
            UriComponentsBuilder uriBuilder) {

        // single-store: server-side luôn ép PLATFORM
        req.setScope(CouponScope.PLATFORM);
        // chuẩn hoá code nếu có
        if (StringUtils.hasText(req.getCode())) req.setCode(req.getCode().trim().toUpperCase());

        CouponResponse created = couponService.create(req);
        URI location = uriBuilder.path("/coupons/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created); // 201
    }

    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public CouponResponse update(@PathVariable Long id, @Valid @RequestBody CreateCouponRequest req) {
        req.setScope(CouponScope.PLATFORM);
        if (StringUtils.hasText(req.getCode())) req.setCode(req.getCode().trim().toUpperCase());
        return couponService.update(id, req);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> setActive(@PathVariable Long id, @RequestParam boolean active) {
        Coupon c = couponRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));
        c.setIsActive(active);
        couponRepository.save(c);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!couponRepository.existsById(id)) return ResponseEntity.notFound().build();
        couponRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/eligible")
    public List<EligibleCouponDTO> listEligibleForCart(@RequestParam @NotNull Long userId) {
        OrderDTO cart = cartService.getCurrentCart(userId);
        if (cart == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found");

        BigDecimal subtotal = cart.getOrderDetails() == null ? ZERO :
                cart.getOrderDetails().stream()
                        .map(od -> {
                            BigDecimal price = od.price() == null ? ZERO : (od.price());
                            int qty = od.quantity() == null ? 0 : od.quantity();
                            return price.multiply(BigDecimal.valueOf(qty));
                        })
                        .reduce(ZERO, BigDecimal::add);

        BigDecimal shipFee = resolveShippingFee(cart);
        LocalDateTime now = LocalDateTime.now();

        List<Coupon> candidates = couponRepository.findAll().stream()
                .filter(Coupon::getIsActive)
                .filter(c -> c.getStartAtUtc() != null && c.getEndAtUtc() != null
                        && !now.isBefore(c.getStartAtUtc()) && !now.isAfter(c.getEndAtUtc()))
                .filter(c -> c.getMinOrderAmount() == null || subtotal.compareTo(c.getMinOrderAmount()) >= 0)
                .toList();

        return candidates.stream()
                .map(c -> toEligibleDTO(c, subtotal, shipFee))
                .sorted(Comparator.comparing(EligibleCouponDTO::getEstimatedSavings, Comparator.nullsLast(BigDecimal::compareTo)).reversed())
                .toList();
    }

    /**
     * Áp dụng coupon theo ID (FE chọn trong modal).
     *
     * POST /coupons/apply/id?userId=123&couponId=456
     */
    @PostMapping("/apply/id")
    public OrderDTO applyCouponById(@RequestParam @NotNull Long userId,
                                    @RequestParam @NotNull Long couponId) {
        Coupon c = couponRepository.findById(couponId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Coupon not found"));

        // Nếu bạn đã có service apply theo entity & tách 2 slot, dùng nó:
        // return cartService.applyCouponByEntity(userId, c);

        // Fallback: áp theo code (dùng chung 1 slot) – chỉ hoạt động nếu coupon có code
        if (!StringUtils.hasText(c.getCode())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Coupon không có mã. Hãy cài đặt CartService.applyCouponByEntity(...) để hỗ trợ apply-by-id.");
        }
        return cartService.applyCoupon(userId, c.getCode());
    }

    /**
     * Áp dụng coupon theo CODE (người dùng gõ tay).
     *
     * POST /coupons/apply/code?userId=123&code=SALE10
     */
    @PostMapping("/apply/code")
    public OrderDTO applyCouponByCode(@RequestParam @NotNull Long userId,
                                      @RequestParam @NotBlank String code) {
        return cartService.applyCoupon(userId, code.trim());
    }

    /**
     * Gỡ coupon đang áp.
     * group = discount | shipping | all
     *
     * DELETE /coupons/apply/{group}?userId=123
     */
    @DeleteMapping("/apply/{group}")
    public OrderDTO removeCoupon(@RequestParam @NotNull Long userId,
                                 @PathVariable String group) {
        // Nếu bạn đã tách 2 slot trong service, thay thế phần dưới bằng:
        // switch (group.toLowerCase()) {
        //   case "discount" -> { return cartService.removeDiscountCoupon(userId); }
        //   case "shipping" -> { return cartService.removeShippingCoupon(userId); }
        //   case "all"      -> { cartService.removeDiscountCoupon(userId); return cartService.removeShippingCoupon(userId); }
        //   default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "group must be discount|shipping|all");
        // }

        // Version cũ (1 slot)
        if (!"discount".equalsIgnoreCase(group) && !"shipping".equalsIgnoreCase(group) && !"all".equalsIgnoreCase(group)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "group must be discount|shipping|all");
        }
        return cartService.removeCoupon(userId);
    }

    // =========================================================
    //                          Helpers
    // =========================================================

    private BigDecimal resolveShippingFee(OrderDTO cart) {
        try {
            if (cart.getTransportId() == null) return ZERO;
            TransportMethod tm = transportRepo.findById(cart.getTransportId()).orElse(null);
            if (tm != null && tm.getPrice() != null) return tm.getPrice();
        } catch (Exception ignored) {}
        return ZERO;
    }

    private EligibleCouponDTO toEligibleDTO(Coupon c, BigDecimal subtotal, BigDecimal shipFee) {
        EligibleCouponDTO dto = new EligibleCouponDTO();
        dto.setId(c.getId());
        dto.setCode(c.getCode());
        dto.setType(c.getType().name());
        dto.setGroup(isShippingCoupon(c) ? "SHIPPING" : "DISCOUNT");
        dto.setDescription(c.getDescription());
        dto.setMinOrderAmount(c.getMinOrderAmount());
        dto.setEstimatedSavings(estimateSavings(c, subtotal, shipFee));
        return dto;
    }

    private boolean isShippingCoupon(Coupon c) {
        return c.getType() == CouponType.FREE_SHIPPING || c.getType() == CouponType.SHIPPING_DISCOUNT;
    }

    private BigDecimal estimateSavings(Coupon c, BigDecimal subtotal, BigDecimal shipFee) {
        BigDecimal maxCap = safe(c.getMaxDiscountValue());
        switch (c.getType()) {
            case PERCENT -> {
                BigDecimal raw = percentage(subtotal, safe(c.getDiscountValue()));
                return cap(raw, maxCap).min(subtotal).max(ZERO);
            }
            case FIXED, CASHBACK_COINS -> {
                BigDecimal raw = safe(c.getDiscountValue());
                return cap(raw, maxCap).min(subtotal).max(ZERO);
            }
            case FREE_SHIPPING -> {
                return cap(safe(shipFee), maxCap).max(ZERO);
            }
            case SHIPPING_DISCOUNT -> {
                BigDecimal raw = safe(c.getDiscountValue()).min(safe(shipFee));
                return cap(raw, maxCap).max(ZERO);
            }
            default -> { return ZERO; }
        }
    }

    private BigDecimal percentage(BigDecimal base, BigDecimal pct) {
        if (base == null || pct == null) return ZERO;
        return base.multiply(pct).divide(BigDecimal.valueOf(100));
    }
    private BigDecimal cap(BigDecimal raw, BigDecimal max) {
        if (raw == null) return ZERO;
        if (max != null && max.signum() > 0) return raw.min(max).max(ZERO);
        return raw.max(ZERO);
    }
    private BigDecimal safe(BigDecimal v) { return v == null ? ZERO : v; }

    // =========================================================
    //                  DTO dùng cho FE (nhẹ)
    // =========================================================
    @Data
    public static class EligibleCouponDTO {
        private Long id;
        private String code;
        private String type;                 // PERCENT | FIXED | ...
        private String group;                // DISCOUNT | SHIPPING
        private String description;
        private BigDecimal minOrderAmount;
        private BigDecimal estimatedSavings; // ước tính giảm để FE sắp xếp/hiển thị
    }
}


