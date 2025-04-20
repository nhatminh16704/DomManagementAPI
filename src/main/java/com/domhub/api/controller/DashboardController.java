package com.domhub.api.controller;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.DashboardDTO;
import com.domhub.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
     private final DashboardService dashboardService;

     @GetMapping
     public ApiResponse<DashboardDTO> getDashboard() {
             return dashboardService.getDashboard();
     }
}
