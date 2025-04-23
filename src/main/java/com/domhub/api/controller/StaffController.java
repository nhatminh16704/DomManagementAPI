package com.domhub.api.controller;

import com.domhub.api.dto.request.ChangePasswordRequest;
import com.domhub.api.dto.request.StaffRequest;
import com.domhub.api.dto.request.UpdateProfileRequest;
import com.domhub.api.dto.response.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.model.Staff;
import com.domhub.api.service.StaffService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/staffs")
@RequiredArgsConstructor
@Validated
public class StaffController {

    private final StaffService staffService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ApiResponse<List<Staff>> getAllStaff() {
        return staffService.getAllStaff();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ApiResponse<Staff> getStaffById(@PathVariable @Min(1) Integer id) {
        return staffService.getStaffById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    @GetMapping("/profile")
    public ApiResponse<Staff> getStaffProfileByAccountId() {
        return staffService.getStaffProfileByAccountId();
    }

    @PutMapping("/profile")
    public ApiResponse<Void> updateStaffProfile(@RequestBody @Valid UpdateProfileRequest updateProfileRequest) {
        return staffService.updateProfile(updateProfileRequest);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> createStaff(@RequestBody @Valid StaffRequest staff) {
        return staffService.createStaff(staff);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> updateStaff(@PathVariable @Min(1) Integer id, @RequestBody @Valid StaffRequest staffRequest) {
        return staffService.updateStaff(id, staffRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteStaff(@PathVariable @Min(1) Integer id) {
        return staffService.deleteStaffById(id);

    }
}

