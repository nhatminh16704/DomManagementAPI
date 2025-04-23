package com.domhub.api.controller;

import com.domhub.api.dto.response.ApiResponse;
import jakarta.validation.Valid;
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

    @GetMapping
    public ApiResponse<List<RegistrationPeriodDTO>> getMethodName() {
        return registrationPeriodService.getAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> createRegistration(@RequestBody @Valid RegistrationPeriodRequest request) {
            return registrationPeriodService.create(request);
    }

    
}
