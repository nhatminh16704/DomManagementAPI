package com.domhub.api.service;

import com.domhub.api.dto.request.ReportRequest;
import com.domhub.api.model.Account;
import com.domhub.api.model.Report;
import com.domhub.api.repository.AccountRepository;
import com.domhub.api.repository.ReportRepository;
import com.domhub.api.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.response.ReportDTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public Report createReport(ReportRequest reportRequest) {
        String authHeader = request.getHeader("Authorization");
        Account account = accountRepository.findById(jwtUtil.extractAccountIdFromHeader(authHeader)).orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại"));
        Report report = new Report();
        report.setCreatedBy(account);
        report.setTitle(reportRequest.getTitle());
        report.setContent(reportRequest.getContent());
        report.setSentDate(LocalDateTime.parse(reportRequest.getSentDate(), formatter));
        report.setStatus(Report.ReportStatus.valueOf(reportRequest.getStatus()));
        return reportRepository.save(report);
    }


    public List<ReportDTO> findAllReports() {
        String authHeader = request.getHeader("Authorization");
        if (jwtUtil.extractRoleFromHeader(authHeader).equals("STUDENT")) {
            return reportRepository.findByCreatedBy_Id(jwtUtil.extractAccountIdFromHeader(authHeader)).stream()
                    .map(report -> new ReportDTO(
                            report.getId(),
                            report.getCreatedBy().toAccountDTO(),
                            report.getTitle(),
                            report.getContent(),
                            report.getSentDate(),
                            report.getStatus().name()
                    ))
                    .collect(Collectors.toList());
        }
        return reportRepository.findAll().stream()
                .map(report -> new ReportDTO(
                        report.getId(),
                        report.getCreatedBy().toAccountDTO(),
                        report.getTitle(),
                        report.getContent(),
                        report.getSentDate(),
                        report.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    public String updateReportStatus(Integer id, String status) {
        Report report = reportRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Report not found with ID: " + id));

        try {
            Report.ReportStatus newStatus = Report.ReportStatus.valueOf(status.toUpperCase());
            report.setStatus(newStatus);
            reportRepository.save(report);
            return "Report status updated successfully to " + newStatus;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
    }

    public String deleteReport(Integer id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Report not found with ID: " + id));
        reportRepository.delete(report);
        return "Report deleted successfully with ID: " + id;
    }
}
