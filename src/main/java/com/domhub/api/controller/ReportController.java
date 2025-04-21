package com.domhub.api.controller;

import com.domhub.api.dto.request.ReportRequest;
import com.domhub.api.dto.request.ReportStatusUpdateRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.ReportDTO;
import com.domhub.api.service.ReportService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Validated
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ApiResponse<Void> createReport(@RequestBody @Valid ReportRequest reportRequest) {
        return reportService.createReport(reportRequest);
    }

    @GetMapping
    public ApiResponse<List<ReportDTO>> findAllReports() {
        return reportService.findAllReports();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<Void> updateReportStatus(
            @PathVariable @Min(1) Integer id,
            @RequestBody @Valid ReportStatusUpdateRequest statusUpdateRequest) {
        return reportService.updateReportStatus(id, statusUpdateRequest.getStatus());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<Void> deleteReport(@PathVariable @Min(1) Integer id) {
        return reportService.deleteReport(id);
    }
}

