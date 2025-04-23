package com.domhub.api.controller;

import com.domhub.api.model.RoomBill;
import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.service.RoomBillService;
import com.domhub.api.service.RoomService;
import com.domhub.api.service.VNPayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.dto.request.PaymentRequest;
import com.domhub.api.model.RoomRental;

import java.util.Map;

import com.domhub.api.service.RoomRentalService;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VNPayController {

    private final VNPayService vnPayService;
    private final RoomService roomService;
    private final RoomRentalRepository roomRentalRepository;
    private final RoomBillService roomBillService;


    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody PaymentRequest request) {
        try {
            roomService.reserveRoom(request.getIdRef());
            String paymentUrl = vnPayService.createPaymentUrl(
                    request.getAmount(),
                    request.getBankCode(),
                    request.getIdRef(),
                    "RENTAL"
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

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @PostMapping("/createBillPayment")
    public ResponseEntity<?> createBillPayment(@RequestBody PaymentRequest request) {
        if (!roomBillService.isBillUnpaid(request.getIdRef())) {
            return ResponseEntity.badRequest().body("Bill is already paid.");
        }
        String paymentUrl = vnPayService.createPaymentUrl(
                request.getAmount(),
                request.getBankCode(),
                request.getIdRef(),
                "BILL"
        );
        return ResponseEntity.ok(paymentUrl);
    }




    @GetMapping("/return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        // Kiểm tra chữ ký bảo mật
        if (!vnPayService.validateSignature(params)) {
            return ResponseEntity.status(400).body("Invalid Signature");
        }

        String rawTxnRef = params.get("vnp_TxnRef");
        if (rawTxnRef.startsWith("BILL_")) {
            // xử lý hóa đơn
            String url = String.format("http://localhost:3000/bills");
            int billId = Integer.parseInt(rawTxnRef.replace("BILL_", ""));
            RoomBill roomBill = roomBillService.findById(billId).orElseThrow(() -> new RuntimeException("Bill not found"));

            // Kiểm tra trạng thái giao dịch
            if ("00".equals(params.get("vnp_ResponseCode")) && "00".equals(params.get("vnp_TransactionStatus"))) {
                roomBill.setStatus(RoomBill.BillStatus.PAID);
                roomBillService.update(roomBill);
                return ResponseEntity.status(302).header("Location", url + "?param=success").build();
            } else {
                return ResponseEntity.status(302).header("Location", url + "?param=fail").build();
            }


        } else {
            // xử lý thuê phòng
            int rentId = Integer.parseInt(rawTxnRef.replace("RENTAL_", ""));
            RoomRental rental = roomRentalRepository.findById(rentId).orElseThrow(() -> new RuntimeException("Room rental not found"));
            String redirectUrl;
            String status;
            // Kiểm tra trạng thái giao dịch
            if ("00".equals(params.get("vnp_ResponseCode")) && "00".equals(params.get("vnp_TransactionStatus"))) {
                rental.setStatus(RoomRental.Status.ACTIVE);
                roomRentalRepository.save(rental);
                status = "success";
            } else {
                status = "failed";
                roomService.cancelRoomRental(rentId);
            }
            redirectUrl = String.format("http://localhost:3000/rooms/%d?status=%s",
            rental.getRoom().getId(), status);
            return ResponseEntity.status(302).header("Location", redirectUrl).build();
        }

        }



}


