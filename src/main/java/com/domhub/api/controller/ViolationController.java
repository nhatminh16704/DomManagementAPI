package com.domhub.api.controller;

import com.domhub.api.dto.request.ViolationRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.model.Violation;
import com.domhub.api.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Min;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RequestMapping("/violations")
@RequiredArgsConstructor
@Validated
public class ViolationController {
    private final ViolationService violationService;

    @GetMapping("/student/{studentId}")
    public ApiResponse<List<Violation>> getViolationsByStudentId(@PathVariable @Min(1) Integer studentId) {
            return violationService.getAllViolationsByStudentId(studentId);
    }

    @PostMapping
    public ApiResponse<Void> createViolation(@RequestBody ViolationRequest violationRequest) {
        return violationService.createViolation(violationRequest);
    }
}
