package com.model1.application.controller.shop;

import com.model1.application.exception.BadRequestException;
import com.model1.application.exception.InternalServerException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final String vnp_TmnCode = "O1WFWCAC";
    private final String vnp_HashSecret = "V883VC8Z3MO3PVU4BV2BYBUK938FIB55";
    private final String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    private final String vnp_ReturnUrl = "http://localhost:8080/payment/return";

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public @ResponseBody String createPayment(@RequestBody Map<String, String> payload) {
        // Kiểm tra các trường bắt buộc
        if (!payload.containsKey("orderId") || !payload.containsKey("amount") || !payload.containsKey("orderInfo")) {
            throw new BadRequestException("Thiếu orderId, amount hoặc orderInfo");
        }

        String vnp_TxnRef = payload.get("orderId");
        String vnp_OrderInfo = payload.get("orderInfo");
        long vnp_Amount;
        try {
            vnp_Amount = Long.parseLong(payload.get("amount"));
        } catch (NumberFormatException e) {
            throw new BadRequestException("Số tiền không hợp lệ");
        }
        String vnp_IpAddr = "127.0.0.1"; // Có thể lấy IP thực từ request nếu cần
        String vnp_Locale = "vn";

        // Tạo tham số VNPay
        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(vnp_Amount));
        vnp_Params.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", "250000");
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

        // Tạo chữ ký
        String signData = getSignData(vnp_Params);
        String vnp_SecureHash;
        try {
            vnp_SecureHash = hmacSHA512(signData, vnp_HashSecret);
        } catch (Exception e) {
            throw new InternalServerException("Lỗi khi tạo chữ ký: " + e.getMessage());
        }
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        // Tạo URL thanh toán
        String queryUrl = vnp_Url + "?" + toQueryString(vnp_Params);
        return queryUrl;
    }

    @GetMapping("/return")
    public String paymentReturnPage() {
        return "payment_return"; // trả về trang payment_return.html hoặc payment_return.thymeleaf
    }

//    @GetMapping("/return")
//    public @ResponseBody String paymentReturn(HttpServletRequest request) {
//        Map<String, String[]> paramMap = request.getParameterMap();
//        Map<String, String> params = new HashMap<>();
//        for (String key : paramMap.keySet()) {
//            params.put(key, request.getParameter(key));
//        }
//
//        // Kiểm tra tham số bắt buộc
//        if (!params.containsKey("vnp_SecureHash") || !params.containsKey("vnp_TxnRef") || !params.containsKey("vnp_ResponseCode")) {
//            throw new BadRequestException("Thiếu tham số cần thiết từ VNPay");
//        }
//
//        String vnp_SecureHash = params.get("vnp_SecureHash");
//        params.remove("vnp_SecureHash");
//
//        String signData = getSignData(new TreeMap<>(params));
//        String serverHash;
//        try {
//            serverHash = hmacSHA512(signData, vnp_HashSecret);
//        } catch (Exception e) {
//            throw new InternalServerException("Lỗi khi xác thực chữ ký: " + e.getMessage());
//        }
//
//        if (!serverHash.equals(vnp_SecureHash)) {
//            throw new BadRequestException("Chữ ký không hợp lệ!");
//        }
//
//        String vnp_ResponseCode = params.get("vnp_ResponseCode");
//        if (!"00".equals(vnp_ResponseCode)) {
//            throw new BadRequestException("Thanh toán thất bại! Mã lỗi: " + vnp_ResponseCode);
//        }
//
//        return "Thanh toán thành công";
//    }

    private String getSignData(Map<String, String> params) {
        StringBuilder signData = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            signData.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue() != null ? entry.getValue() : "", StandardCharsets.UTF_8))
                    .append("&");
        }
        return signData.substring(0, signData.length() - 1);
    }

    private String hmacSHA512(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA512");
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        mac.init(keySpec);
        byte[] bytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(bytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String toQueryString(Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            query.append(entry.getKey())
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue() != null ? entry.getValue() : "", StandardCharsets.UTF_8))
                    .append("&");
        }
        return query.substring(0, query.length() - 1);
    }
}