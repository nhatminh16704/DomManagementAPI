package com.domhub.api.controller;

import com.domhub.api.dto.response.ReportDTO;
import com.domhub.api.model.Report;
import com.domhub.api.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('STUDENT', 'STAFF', 'ADMIN')")
    public ResponseEntity<String> createReport(@RequestBody Report report) {
        String createdReport = reportService.createReport(report);
        return ResponseEntity.ok(createdReport);
    }

    @GetMapping("/findAll")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<ReportDTO>> findAllReports() {
        List<ReportDTO> reports = reportService.findAllReports();
        return ResponseEntity.ok(reports);
    }

}
