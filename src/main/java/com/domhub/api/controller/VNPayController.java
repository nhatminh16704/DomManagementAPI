package com.domhub.api.controller;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.model.RoomBill;
import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.service.RoomBillService;
import com.domhub.api.service.RoomService;
import com.domhub.api.service.VNPayService;
import jakarta.validation.Valid;
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
    @PostMapping("/room-rental")
    public ApiResponse<String> createRoomRentalPayment(@RequestBody @Valid PaymentRequest request) {
        return vnPayService.createRoomRentalPayment(request);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @PostMapping("/bill-payment")
    public ApiResponse<String> createBillPayment(@RequestBody @Valid PaymentRequest request) {
        return vnPayService.createBillPayment(request);
    }


    @GetMapping("/return")
    public ResponseEntity<?> vnpayReturn(@RequestParam Map<String, String> params) {
        String redirectUrl = vnPayService.handleVnPayReturn(params);
        return ResponseEntity.status(303).header("Location", redirectUrl).build();
    }



}


