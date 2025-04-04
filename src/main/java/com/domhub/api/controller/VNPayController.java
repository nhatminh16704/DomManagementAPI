package com.domhub.api.controller;

import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.service.RoomService;
import com.domhub.api.service.VNPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.dto.request.PaymentRequest;
import com.domhub.api.model.RoomRental;

import java.util.Map;

import com.domhub.api.service.RoomRentalService;

@RestController
@RequestMapping("/vnpay")

public class VNPayController {

    private final VNPayService vnPayService;
    private final RoomService roomService;
    private final RoomRentalRepository roomRentalRepository;

    public VNPayController(VNPayService vnPayService, RoomService roomService, RoomRentalRepository roomRentalRepository) {
        this.vnPayService = vnPayService;
        this.roomService = roomService;
        this.roomRentalRepository = roomRentalRepository;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        try {
            roomService.reserveRoom(request.getIdRef());
            String paymentUrl = vnPayService.createPaymentUrl(
                    request.getAmount(),
                    request.getBankCode(),
                    request.getIdRef()
            );
            return ResponseEntity.ok(paymentUrl);

        } catch (RuntimeException e) {
            // Check if the error is "Room rental not found"
            if (e.getMessage().contains("Room not found") || e.getMessage().contains("No available rooms left.")) {
                roomService.cancelRoomRental(request.getIdRef());
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping("/return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        System.out.println("VNPay Return Params: " + params);

        // Kiểm tra chữ ký bảo mật
        boolean isValid = vnPayService.validateSignature(params);

        if (!isValid) {
            return ResponseEntity.status(400).body("Invalid Signature");
        }

        // Lấy ID đơn thuê phòng từ txnRef
        Integer rentalId = Integer.parseInt(params.get("vnp_TxnRef"));
        String redirectUrl;
        String status;
        RoomRental rental = roomRentalRepository.findById(rentalId).orElseThrow(() -> new RuntimeException("Room rental not found"));
        // Kiểm tra trạng thái giao dịch
        if ("00".equals(params.get("vnp_ResponseCode")) && "00".equals(params.get("vnp_TransactionStatus"))) {
            rental.setStatus(RoomRental.Status.ACTIVE);
            roomRentalRepository.save(rental);
            status = "success";
            // return ResponseEntity.ok("Payment Successful! Order ID: " + params.get("vnp_TxnRef"));
        } else {
            status = "failed";
            roomService.cancelRoomRental(rentalId);
            // return ResponseEntity.status(400).body("Payment Failed! Error Code: " + params.get("vnp_ResponseCode"));
        }
        redirectUrl = String.format("http://localhost:3000/rooms/%d?status=%s", 
        rental.getRoomId(), status);
        return ResponseEntity.status(302).header("Location", redirectUrl).build();
    }


}


