//package com.example.Project.service.impl;
//
//import com.example.Project.config.PaymentStatus;
//import com.example.Project.config.VnPayProperties;
//import com.example.Project.dto.VnPayResult;
//import com.example.Project.entity.Order;
//import com.example.Project.entity.PaymentMethod;
//import com.example.Project.repository.OrderRepository;
//import com.example.Project.repository.PaymentMethodRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.security.InvalidKeyException;
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Map;
//import java.util.Optional;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class VnPayService {
//
//    private final VnPayProperties props;
//    private final OrderRepository orderRepo;
//    private final PaymentMethodRepository methodRepo;
//
//    private static final DateTimeFormatter VN_DATETIME = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
//    private static final ZoneId VN_TZ = ZoneId.of("Asia/Ho_Chi_Minh");
//
//    /**
//     * VNPay redirect về. Xác minh checksum, cập nhật trạng thái đơn và trả kết quả gọn.
//     */
//    public VnPayResult handleReturn(Map<String, String> q) {
//        log.debug("[VNPay] RETURN received params: {}", q);
//        log.debug("[VNPay] Using hashSecret: [{}] (length: {})", props.getHashSecret(), props.getHashSecret().length());
//
//        if (!isSignatureValid(q)) {
//            log.warn("[VNPay] RETURN invalid signature. raw={}", q);
//            return new VnPayResult(null, null, 0, "failed", "Invalid checksum");
//        }
//
//        String respCode = q.getOrDefault("vnp_ResponseCode", "");
//        String txnRef = q.getOrDefault("vnp_TxnRef", "");
//        long amount = parseLong(q.get("vnp_Amount")) / 100;
//
//        Optional<Order> opt = orderRepo.findByIdOrders(txnRef);
//        if (opt.isEmpty()) {
//            log.warn("[VNPay] RETURN: order not found, idOrders={}", txnRef);
//            return new VnPayResult(null, txnRef, 0, "failed", "Order not found");
//        }
//
//        Order order = opt.get();
//
//        if ("00".equals(respCode)) {
//            long orderTotal = order.getTotalMoney() == null ? 0 : order.getTotalMoney().longValue();
//            long paid = Math.min(Math.max(orderTotal, 0), Math.max(amount, 0));
//
//            methodRepo.findByNameAndIsDeleteFalse("VNPAY").ifPresent(order::setPaymentMethod);
//            order.setPaidAmount(paid);
//            order.setPaidAt(LocalDateTime.now(VN_TZ));
//            order.setPaymentStatus(PaymentStatus.PAID);
//            orderRepo.save(order);
//
//            log.info("[VNPay] RETURN: PAID ok. orderCode={}, amount={}", order.getIdOrders(), paid);
//            return new VnPayResult(order.getId(), order.getIdOrders(), paid, "success", "OK");
//        }
//
//        order.setPaymentStatus(PaymentStatus.FAILED);
//        orderRepo.save(order);
//        log.info("[VNPay] RETURN: FAILED rspCode={}, orderCode={}", respCode, order.getIdOrders());
//        return new VnPayResult(order.getId(), order.getIdOrders(), 0, "failed", "VNPay response: " + respCode);
//    }
//
//    /**
//     * FE chỉ gửi idOrders & bankCode; số tiền luôn lấy từ DB.
//     */
//    public String createPaymentUrl(String idOrders, long ignoredAmountVnd, String clientIp, String bankCodeOpt) {
//        Order order = orderRepo.findByIdOrders(idOrders)
//                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + idOrders));
//
//        long amountVnd = order.getTotalMoney() == null ? 0L : order.getTotalMoney().longValue();
//        if (amountVnd <= 0) {
//            throw new IllegalStateException("Order total must be > 0 before creating VNPay payment.");
//        }
//
//        PaymentMethod pm = methodRepo.findByNameAndIsDeleteFalse("VNPAY")
//                .orElseThrow(() -> new IllegalStateException("Payment method VNPAY not found"));
//
//        order.setPaymentMethod(pm);
//        order.setPaymentStatus(PaymentStatus.PENDING);
//        orderRepo.save(order);
//
//        String txnRef = idOrders.length() > 100 ? idOrders.substring(0, 100) : idOrders;
//
//        ZonedDateTime now = ZonedDateTime.now(VN_TZ);
//        String createDate = VN_DATETIME.format(now);
//        String expireDate = VN_DATETIME.format(now.plusMinutes(15));
//
//        SortedMap<String, String> vnp = new TreeMap<>();
//        vnp.put("vnp_Version", props.getVersion());
//        vnp.put("vnp_Command", props.getCommand());
//        vnp.put("vnp_TmnCode", props.getTmnCode());
//        vnp.put("vnp_Amount", String.valueOf(amountVnd * 100));
//        vnp.put("vnp_CurrCode", props.getCurrCode());
//        vnp.put("vnp_TxnRef", txnRef);
//        vnp.put("vnp_OrderInfo", ("Thanh toan don " + idOrders).trim());
//        vnp.put("vnp_OrderType", props.getOrderType());
//        vnp.put("vnp_Locale", props.getLocale());
//        vnp.put("vnp_ReturnUrl", props.getReturnUrl().trim());
//        if (props.getIpnUrl() != null && !props.getIpnUrl().isBlank()) {
//            vnp.put("vnp_IpnUrl", props.getIpnUrl());
//        }
//        vnp.put("vnp_IpAddr", (clientIp == null || clientIp.isBlank()) ? "127.0.0.1" : clientIp);
//        vnp.put("vnp_CreateDate", createDate);
//        vnp.put("vnp_ExpireDate", expireDate);
//        if (bankCodeOpt != null && !bankCodeOpt.isBlank()) vnp.put("vnp_BankCode", bankCodeOpt.trim());
//
//        String hashData  = buildHashData(vnp);
//        String secureHash = hmacSHA512(props.getHashSecret(), hashData);
//
//        log.debug("[VNPay] Hash data for signing: {}", hashData);
//        log.debug("[VNPay] Generated hash: {}", secureHash);
//
//        String query = buildQueryString(vnp)
//                + "&vnp_SecureHashType=HmacSHA512"
//                + "&vnp_SecureHash=" + secureHash;
//
//        String finalUrl = props.getPayUrl() + "?" + query;
//        log.debug("[VNPay] Final payment URL: {}", finalUrl);
//
//        return finalUrl;
//    }
//
//    /**
//     * IPN từ VNPay
//     */
//    public VnpIpnResult handleIpn(Map<String, String> allParams) {
//        log.debug("[VNPay] IPN received params: {}", allParams);
//
//        if (!isSignatureValid(allParams)) {
//            log.warn("[VNPay] IPN invalid signature. raw={}", allParams);
//            return new VnpIpnResult("97", "Invalid signature");
//        }
//
//        String txnRef = allParams.get("vnp_TxnRef");
//        String rspCode = allParams.get("vnp_ResponseCode");
//        String transStat = allParams.get("vnp_TransactionStatus");
//        long amount = parseLong(allParams.get("vnp_Amount")) / 100;
//
//        Order order = orderRepo.findByIdOrders(txnRef).orElse(null);
//        if (order == null) return new VnpIpnResult("01", "Order not found");
//
//        if (order.getPaymentStatus() == PaymentStatus.PAID) return new VnpIpnResult("00", "Order already paid");
//
//        if ("00".equals(rspCode) && "00".equals(transStat)) {
//            long orderTotal = order.getTotalMoney() == null ? 0 : order.getTotalMoney().longValue();
//            long paid = Math.min(Math.max(orderTotal, 0), Math.max(amount, 0));
//
//            order.setPaymentStatus(PaymentStatus.PAID);
//            order.setPaidAmount(paid);
//
//            String payDate = allParams.get("vnp_PayDate");
//            if (payDate != null && payDate.matches("\\d{14}")) {
//                order.setPaidAt(LocalDateTime.parse(payDate, VN_DATETIME));
//            } else {
//                order.setPaidAt(LocalDateTime.now(VN_TZ));
//            }
//            orderRepo.save(order);
//            log.info("[VNPay] IPN: PAID ok. orderCode={}, amount={}", order.getIdOrders(), paid);
//            return new VnpIpnResult("00", "Confirm Success");
//        } else {
//            order.setPaymentStatus(PaymentStatus.FAILED);
//            orderRepo.save(order);
//            log.info("[VNPay] IPN: FAILED rspCode={}, orderCode={}", rspCode, order.getIdOrders());
//            return new VnpIpnResult("00", "Confirm Success (failed stored)");
//        }
//    }
//
//    public boolean verifyReturn(Map<String, String> allParams) {
//        return isSignatureValid(allParams);
//    }
//
//    // ==================== Sign utils ====================
//
//    private boolean isSignatureValid(Map<String, String> params) {
//        if (params == null || params.isEmpty()) return false;
//        String receivedHash = params.get("vnp_SecureHash");
//        if (receivedHash == null || receivedHash.isBlank()) return false;
//
//        // Loại bỏ vnp_SecureHash khỏi params để tính hash
//        SortedMap<String, String> canonical = new TreeMap<>();
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            if (key != null && value != null &&
//                    !key.equals("vnp_SecureHash") &&
//                    !key.equals("vnp_SecureHashType") &&
//                    key.startsWith("vnp_") &&
//                    !value.trim().isEmpty()) {
//                canonical.put(key, value);
//            }
//        }
//
//        String data = buildHashData(canonical);
//        String myHash = hmacSHA512(props.getHashSecret(), data);
//
//        log.debug("[VNPay] Received hash: {}", receivedHash);
//        log.debug("[VNPay] Computed hash: {}", myHash);
//        log.debug("[VNPay] Hash data used: {}", data);
//        log.debug("[VNPay] Canonical params: {}", canonical);
//
//        return receivedHash.equalsIgnoreCase(myHash);
//    }
//
//    /**
//     * Build hash data theo tài liệu VNPay - KHÔNG URL encode value khi tính hash
//     * FIXED: Không encode value cho hash data
//     */
//    private static String buildHashData(SortedMap<String, String> params) {
//        StringBuilder sb = new StringBuilder();
//        for (Map.Entry<String, String> e : params.entrySet()) {
//            if (e.getValue() == null || e.getValue().isBlank()) continue;
//            if (sb.length() > 0) sb.append('&');
//            sb.append(e.getKey()).append('=').append(enc(e.getValue().trim()));
//        }
//        return sb.toString();
//    }
//
//    /**
//     * Build query string cho URL - CẦN URL encode
//     */
//    private static String buildQueryString(SortedMap<String, String> params) {
//        StringBuilder sb = new StringBuilder();
//        boolean first = true;
//        for (var e : new TreeMap<>(params).entrySet()) {
//            if (!first) sb.append('&'); first = false;
//            sb.append(enc(e.getKey())).append('=').append(enc(e.getValue()));
//        }
//        return buildHashData(params);
//    }
//
//    private static String enc(String s) {
//        try {
//            return URLEncoder.encode(s, StandardCharsets.UTF_8)
//                    .replace("+", "%20")
//                    .replace("%7E", "~");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private static String hmacSHA512(String secretKey, String data) {
//        try {
//            Mac hmac = Mac.getInstance("HmacSHA512");
//            SecretKeySpec keySpec = new SecretKeySpec(secretKey.trim().getBytes(StandardCharsets.UTF_8), "HmacSHA512");
//            hmac.init(keySpec);
//            byte[] raw = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
//            StringBuilder sb = new StringBuilder(raw.length * 2);
//            for (byte b : raw) sb.append(String.format("%02X", b)); // UPPERCASE
//            return sb.toString();
//        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
//            throw new RuntimeException("Error generating HMAC-SHA512", e);
//        }
//    }
//
//
//    private static long parseLong(String s) {
//        try {
//            return Long.parseLong(s);
//        } catch (Exception e) {
//            return 0L;
//        }
//    }
//
//    public record VnpIpnResult(String RspCode, String Message) {
//    }
//}