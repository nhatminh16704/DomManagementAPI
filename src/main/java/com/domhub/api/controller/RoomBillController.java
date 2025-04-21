package com.domhub.api.controller;

import com.domhub.api.dto.request.ElectricityUpdateRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.RoomBillDTO;
import com.domhub.api.model.RoomBill;
import com.domhub.api.service.RoomBillService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
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
    public ApiResponse<List<RoomBillDTO>> getBills(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate month,
            @RequestParam(required = false)
            @Pattern(regexp = "PAID|UNPAID|PENDING", message = "Status must be one of: PAID, UNPAID, PENDING")
            String status

    ) {
        if (month != null && status != null) {
            return roomBillService.getAllByMonthAndStatus(month, status);
        }
        return roomBillService.getAll();
    }



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
    public ApiResponse<Void> updateElectricity(@RequestBody @Valid ElectricityUpdateRequest request) {
        return roomBillService.updateElectricityEnd(request);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/student")
    public ApiResponse<List<RoomBillDTO>> getStudentBills() {
        return roomBillService.getStudentBills();

    }





}

