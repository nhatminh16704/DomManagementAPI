package com.domhub.api.service;

import com.domhub.api.dto.request.PaymentRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.model.RoomBill;
import com.domhub.api.model.RoomRental;
import com.domhub.api.repository.RoomRentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Service
public class VNPayService {

    private final RoomService roomService;
    private final RoomBillService roomBillService;
    private final RoomRentalRepository roomRentalRepository;
    private final RoomRentalService roomRentalService;

    @Value("${vnpay.pay-url}")
    private String vnp_PayUrl;

    @Value("${vnpay.return-url}")
    private String vnp_ReturnUrl;

    @Value("${vnpay.tmn-code}")
    private String vnp_TmnCode;

    @Value("${vnpay.secret-key}")
    private String secretKey;

    public String createPaymentUrl(int amount, String bankCode, Integer idRef, String type) {
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Convert to VNPay format
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_Locale", "vn");

        if (bankCode != null && !bankCode.isEmpty()) {
            vnp_Params.put("vnp_BankCode", bankCode);
        }

        String txnRef = type + "_" + idRef; // "RENT_12" hoặc "BILL_34"
        vnp_Params.put("vnp_TxnRef", txnRef); // Transaction reference (unique ID for tracking)
        vnp_Params.put("vnp_OrderInfo", "Thanh toán đơn hàng");

        vnp_Params.put("vnp_OrderType", "other"); // Default order type

        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_CreateDate", getCurrentTime());
        vnp_Params.put("vnp_ExpireDate", getExpireTime());

        vnp_Params.put("vnp_IpAddr", "127.0.0.1"); //Default IP address

        // Build query string
        String queryUrl = getQueryString(vnp_Params);

        // Generate SecureHash
        String secureHash = hmacSHA512(secretKey, queryUrl);
        vnp_Params.put("vnp_SecureHash", secureHash);

        // Build final URL
        return vnp_PayUrl + "?" + getQueryString(vnp_Params);
    }


    private String getQueryString(Map<String, String> params) {
        List<String> fieldNames = new ArrayList<>(params.keySet());
        Collections.sort(fieldNames);
        StringBuilder query = new StringBuilder();
        for (String fieldName : fieldNames) {
            String fieldValue = params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                try {
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8))
                            .append("=")
                            .append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8))
                            .append("&");
                } catch (Exception e) {
                    throw new RuntimeException("Error encoding URL parameter: " + fieldName);
                }
            }
        }
        return query.substring(0, query.length() - 1); // Remove the last "&"
    }

    private String hmacSHA512(String key, String data) {
        try {
            Mac hmac512 = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac512.init(secretKeySpec);
            byte[] result = hmac512.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString().trim(); // Trim để tránh lỗi
        } catch (Exception e) {
            throw new RuntimeException("Error while generating HMAC SHA512", e);
        }
    }


    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        return new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
    }

    private String getExpireTime() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        calendar.add(Calendar.MINUTE, 10);
        return new SimpleDateFormat("yyyyMMddHHmmss").format(calendar.getTime());
    }

    public boolean validateSignature(Map<String, String> params) {
        String receivedSecureHash = params.remove("vnp_SecureHash");

        String queryString = getQueryString(params);

        // Generate HMAC SHA512 hash
        String calculatedHash = hmacSHA512(secretKey, queryString);

        // Compare with the received SecureHash
        return receivedSecureHash != null && receivedSecureHash.equalsIgnoreCase(calculatedHash);
    }

    public ApiResponse<String> createRoomRentalPayment(PaymentRequest request) {
        roomService.reserveRoom(request.getIdRef());
        String paymentUrl = createPaymentUrl(
                request.getAmount(),
                request.getBankCode(),
                request.getIdRef(),
                "RENTAL"
        );
        return ApiResponse.success(paymentUrl, "Payment URL generated successfully");
    }

    public ApiResponse<String> createBillPayment(PaymentRequest request) {
        if(!roomBillService.existsById(request.getIdRef())) {
            throw new AppException(ErrorCode.ROOM_BILL_NOT_FOUND);
        }

        if (!roomBillService.isBillUnpaid(request.getIdRef())) {
            throw new AppException(ErrorCode.ROOM_BILL_ALREADY_PAID);
        }

        String paymentUrl = createPaymentUrl(
                request.getAmount(),
                request.getBankCode(),
                request.getIdRef(),
                "BILL"
        );

        return ApiResponse.success(paymentUrl, "Payment URL generated successfully");
    }

    public String handleVnPayReturn(Map<String, String> params) {
        if (!validateSignature(params)) {
            throw new AppException(ErrorCode.INVALID_SIGNATURE);
        }

        String rawTxnRef = params.get("vnp_TxnRef");
        boolean isSuccess = "00".equals(params.get("vnp_ResponseCode")) &&
                "00".equals(params.get("vnp_TransactionStatus"));

        if (rawTxnRef.startsWith("BILL_")) {
            int billId = Integer.parseInt(rawTxnRef.replace("BILL_", ""));
            RoomBill roomBill = roomBillService.findById(billId)
                    .orElseThrow(() -> new RuntimeException("Bill not found"));

            if (isSuccess) {
                roomBill.setStatus(RoomBill.BillStatus.PAID);
                roomBillService.update(roomBill);
                return "http://localhost:3000/bills?status=success";
            } else {
                return "http://localhost:3000/bills?status=fail";
            }

        } else if (rawTxnRef.startsWith("RENTAL_")) {
            int rentId = Integer.parseInt(rawTxnRef.replace("RENTAL_", ""));
            RoomRental rental = roomRentalService.getRoomRentalById(rentId);

            if (isSuccess) {
                rental.setStatus(RoomRental.Status.ACTIVE);
                roomRentalRepository.save(rental);
                return String.format("http://localhost:3000/rooms/%d?status=success", rental.getRoom().getId());
            } else {
                roomService.cancelRoomRental(rentId);
                return String.format("http://localhost:3000/rooms/%d?status=failed", rental.getRoom().getId());
            }
        }

        throw new IllegalArgumentException("Unknown transaction reference");
    }


}

