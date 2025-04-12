package com.domhub.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.domhub.api.dto.request.NotificationRequest;
import com.domhub.api.dto.request.RegistrationPeriodRequest;
import com.domhub.api.dto.response.RegistrationPeriodDTO;
import com.domhub.api.model.RegistrationPeriod;
import com.domhub.api.repository.ReportRepository;
import com.domhub.api.service.RegistrationPeriodService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/registrations")
@RequiredArgsConstructor
public class RegistrationPeriodController {

    private final RegistrationPeriodService registrationPeriodService;

    @GetMapping("/getAll")
    public List<RegistrationPeriodDTO> getMethodName() {
        return registrationPeriodService.getAll();
    }

    @PostMapping("/create")      
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createRegistrantion(@RequestBody RegistrationPeriodRequest request) {
        try {
            String id = registrationPeriodService.create(request);
            return ResponseEntity.ok("Created successfully with ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Creation failed: " + e.getMessage());
        }
    }

    
}
