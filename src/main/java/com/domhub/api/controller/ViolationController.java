package com.domhub.api.controller;

import com.domhub.api.dto.request.ViolationRequest;
import com.domhub.api.dto.response.ViolationDTO;
import com.domhub.api.model.Violation;
import com.domhub.api.service.ViolationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
@RequestMapping("/violations")
@RequiredArgsConstructor
public class ViolationController {
    private final ViolationService violationService;

//    @GetMapping("/student/{studentId}")
//    public ResponseEntity<?> getViolationsByStudentId(@PathVariable Integer studentId) {
//            List<ViolationDTO> violationList = violationService.getAllViolationsByStudentId(studentId);
//            return ResponseEntity.ok(violationList);
//    }

    @PostMapping
    public ResponseEntity<?> createViolation(@RequestBody ViolationRequest violationRequest) {
        Violation violation = violationService.createViolation(violationRequest);
        return ResponseEntity.ok(violation);
    }
}
