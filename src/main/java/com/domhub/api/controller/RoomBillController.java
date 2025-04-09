package com.domhub.api.controller;

import com.domhub.api.dto.request.ElectricityUpdateRequest;
import com.domhub.api.dto.response.RoomBillDTO;
import com.domhub.api.model.RoomBill;
import com.domhub.api.service.RoomBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/room-bills")
@RequiredArgsConstructor
public class RoomBillController {

    private final RoomBillService roomBillService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RoomBillDTO> getBills(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate month,
            @RequestParam(required = false) RoomBill.BillStatus status
    ) {
        if (month != null && status != null) {
            return roomBillService.getAllByMonthAndStatus(month, status);
        }
        return roomBillService.getAll();
    }

    // Tạo hóa đơn mới - chỉ ADMIN được gọi
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public RoomBill createBill(@RequestBody RoomBill bill) {
        return roomBillService.create(bill);
    }

    // Lấy bill theo phòng và tháng - user thường cũng có thể xem (không cần restrict)
    @GetMapping("/{roomId}")
    public RoomBill getByRoomAndMonth(
            @PathVariable Integer roomId,
            @RequestParam("month") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate month
    ) {
        return roomBillService.getByRoomIdAndMonth(roomId, month)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bill."));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/electricity")
    public ResponseEntity<RoomBill> updateElectricity(@RequestBody ElectricityUpdateRequest request) {
        RoomBill updated = roomBillService.updateElectricityEnd(
                request.getRoomId(),
                request.getBillMonth(),
                request.getNewEnd()
        );
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student")
    public ResponseEntity<List<RoomBillDTO>> getStudentBills(
            @RequestHeader("Authorization") String authHeader) {

        List<RoomBillDTO> bills = roomBillService.getStudentBills(authHeader);
        return ResponseEntity.ok(bills);
    }





}

