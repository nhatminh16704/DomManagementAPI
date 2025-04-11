package com.domhub.api.controller;

import com.domhub.api.dto.request.ReportRequest;
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
    public ResponseEntity<?> createReport(@RequestBody ReportRequest reportRequest) {
        try {
            Report report = reportService.createReport(reportRequest);
            return ResponseEntity.ok(report);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/findAll")
    public ResponseEntity<List<ReportDTO>> findAllReports() {
        List<ReportDTO> reports = reportService.findAllReports();
        return ResponseEntity.ok(reports);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<String> updateReportStatus(
            @PathVariable Integer id,
            @RequestBody ReportStatusUpdateRequest statusUpdateRequest) {
        String message = reportService.updateReportStatus(id, statusUpdateRequest.status());
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<String> deleteReport(@PathVariable Integer id) {
        String message = reportService.deleteReport(id);
        return ResponseEntity.ok(message);
    }
}

record ReportStatusUpdateRequest(String status) {}
