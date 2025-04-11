package com.domhub.api.controller;

import com.domhub.api.dto.request.ChangePasswordRequest;
import com.domhub.api.dto.request.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.model.Staff;
import com.domhub.api.service.StaffService;

import java.util.List;
import java.util.Optional;

@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RequestMapping("/staffs")
@RequiredArgsConstructor
public class StaffController {

    private final StaffService staffService;

    @GetMapping("/findAll")
    public ResponseEntity<List<Staff>> getAllStaff() {
        System.out.println("Calling getAllStaff");
        List<Staff> staffList = staffService.getAllStaff();
        return ResponseEntity.ok(staffList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Staff> getStaffById(@PathVariable Integer id) {
        Optional<Staff> staff = staffService.getStaffById(id);
        return staff.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/profile")
    public ResponseEntity<Staff> getStaffProfileByAccountId() {
        Staff staff = staffService.getStaffProfileByAccountId();
        return ResponseEntity.ok(staff);
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateStaffProfile(@RequestBody UpdateProfileRequest updateProfileRequest) {
        try {
            String result = staffService.updateProfile(updateProfileRequest);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/password")
    public ResponseEntity<?> updateStaffPasswordByAccountId(@RequestBody ChangePasswordRequest changePasswordRequest) {
        try {
            String result = staffService.changePassword(changePasswordRequest);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Staff> createStaff(@RequestBody Staff staff) {
        Staff newStaff = staffService.createStaff(staff);
        return ResponseEntity.ok(newStaff);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateStaff(@PathVariable Integer id, @RequestBody Staff staff) {
        try {
            String result = staffService.updateStaff(id, staff);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteStaff(@PathVariable Integer id) {
        try {
            staffService.deleteStaffById(id);
            return ResponseEntity.ok("Deleted staff with id " + id);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

