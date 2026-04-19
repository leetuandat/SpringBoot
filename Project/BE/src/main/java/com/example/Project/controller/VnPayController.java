package com.example.Project.controller;

import com.example.Project.config.VNPayUtils;
import com.example.Project.config.VnPayProperties;
import com.example.Project.dto.VnPayResult;
import com.example.Project.entity.Order;
import com.example.Project.repository.OrderRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/payments/vnpay")
@RequiredArgsConstructor
public class VnPayController {

    private final VNPayUtils vnPayUtils;       // ✅ inject utils
    private final VnPayProperties properties;  // ✅ inject config
    private final OrderRepository orderRepository;

    /** VNPay redirect về đây (BE). BE verify & cập nhật rồi trả JSON cho FE. */
    @GetMapping("/return")
    public ResponseEntity<VnPayResult> vnpReturn(@RequestParam Map<String, String> params) {
        log.info("[VnPayController] Return endpoint called with params: {}", params);

        boolean valid = verifyReturn(params);
        VnPayResult rs = new VnPayResult();
        rs.setSuccess(valid && "00".equals(params.get("vnp_ResponseCode")));
        rs.setMessage(valid ? "Thanh toán thành công" : "Sai chữ ký hoặc lỗi giao dịch");
        rs.setTransactionId(params.get("vnp_TransactionNo"));
        rs.setOrderId(Long.valueOf(params.get("vnp_TxnRef")));

        return ResponseEntity.ok(rs);
    }

    /** FE tạo phiên thanh toán — chỉ cần idOrders & bankCode; số tiền lấy từ DB. */
    @PostMapping("/create")
    public Map<String, Object> create(@RequestBody CreateReq req, HttpServletRequest http) throws Exception {
        log.info("[VnPayController] Create payment request: {}", req);

        if (req.getIdOrders() == null || req.getIdOrders().trim().isEmpty()) {
            throw new IllegalArgumentException("idOrders is required");
        }

        Order order = orderRepository.findByIdOrders(req.getIdOrders())
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + req.getIdOrders()));

        // ✅ Lấy tổng tiền
        long subTotal = order.getSubTotal() != null ? order.getSubTotal().longValue() : 0L;
        long shippingFee = order.getShippingFee() != null ? order.getShippingFee().longValue() : 0L;
        long discount = order.getDiscountAmount() != null ? order.getDiscountAmount().longValue() : 0L;
        long shippingDiscount = order.getShippingDiscountAmount() != null ? order.getShippingDiscountAmount().longValue() : 0L;

        long amount = (subTotal + shippingFee);
        if (amount < 5000 || amount >= 1_000_000_000) {
            throw new IllegalArgumentException("Số tiền không hợp lệ: " + amount);
        }

        String ip = clientIp(http);

        // Build params
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Version", properties.getVersion());
        params.put("vnp_Command", properties.getCommand());
        params.put("vnp_TmnCode", properties.getTmnCode());
        params.put("vnp_Amount", String.valueOf(amount * 100)); // demo, lấy amount từ DB
        params.put("vnp_CurrCode", properties.getCurrCode());
        params.put("vnp_TxnRef", req.getIdOrders());
        params.put("vnp_OrderInfo", "Thanh toan don hang: " + req.getIdOrders());
        params.put("vnp_OrderType", properties.getOrderType());
        params.put("vnp_Locale", properties.getLocale());
        params.put("vnp_IpAddr", ip);
        params.put("vnp_ReturnUrl", properties.getReturnUrl());
        params.put("vnp_CreateDate", new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        if (req.getBankCode() != null && !req.getBankCode().isBlank()) {
            params.put("vnp_BankCode", req.getBankCode());
        }

        // ✅ ký hash
        String signValue = vnPayUtils.hashAllFields(params);
        params.put("vnp_SecureHash", signValue);

        // build URL
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (query.length() > 0) query.append("&");
            query.append(entry.getKey()).append("=").append(entry.getValue());
        }
        String url = properties.getPayUrl() + "?" + query;

        return Map.of("paymentUrl", url);
    }

    /** Debug endpoint để test signature */
    @GetMapping("/debug-signature")
    public Map<String, Object> debugSignature(@RequestParam Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("receivedParams", params);
        result.put("isValid", verifyReturn(params));
        result.put("receivedHash", params.get("vnp_SecureHash"));
        return result;
    }

    /** IPN */
    @RequestMapping(
            value = "/ipn",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, String> ipn(@RequestParam Map<String, String> params) {
        boolean valid = verifyReturn(params);
        return Map.of(
                "RspCode", valid ? "00" : "97",
                "Message", valid ? "Confirm Success" : "Invalid signature"
        );
    }

    private static String clientIp(HttpServletRequest req) {
        String h = req.getHeader("X-Forwarded-For");
        if (h != null && !h.isBlank()) return h.split(",")[0].trim();
        h = req.getHeader("X-Real-IP");
        return (h != null && !h.isBlank()) ? h : req.getRemoteAddr();
    }

    private boolean verifyReturn(Map<String, String> params) {
        try {
            String receivedHash = params.get("vnp_SecureHash");
            Map<String, String> copy = new HashMap<>(params);
            copy.remove("vnp_SecureHash");
            copy.remove("vnp_SecureHashType");
            String calculated = vnPayUtils.hashAllFields(copy);
            return receivedHash != null && receivedHash.equalsIgnoreCase(calculated);
        } catch (Exception e) {
            return false;
        }
    }

    @Data
    public static class CreateReq {
        private String idOrders;
        private String bankCode; // optional
    }
}
