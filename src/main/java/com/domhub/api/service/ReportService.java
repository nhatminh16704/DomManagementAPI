package com.domhub.api.service;

import com.domhub.api.dto.request.ReportRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.mapper.AccountMapper;
import com.domhub.api.mapper.ReportMapper;
import com.domhub.api.model.Account;
import com.domhub.api.model.Report;
import com.domhub.api.repository.AccountRepository;
import com.domhub.api.repository.ReportRepository;
import com.domhub.api.security.JwtUtil;
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
    private final HttpServletRequest httpServletRequest;
    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;
    private final AccountService accountService;
    private final ReportMapper reportMapper;

    public ApiResponse<Void> createReport(ReportRequest reportRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        Account account = accountRepository.findById(jwtUtil.extractAccountIdFromHeader(authHeader))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Report report = reportMapper.toEntity(reportRequest);
        report.setCreatedBy(account);
        reportRepository.save(report);

        return ApiResponse.success("Report created successfully");
    }


    public ApiResponse<List<ReportDTO>> findAllReports() {

        String authHeader = httpServletRequest.getHeader("Authorization");
        accountService.validateAccountExists(jwtUtil.extractAccountIdFromHeader(authHeader), "Account not found");

        if (jwtUtil.extractRoleFromHeader(authHeader).equals("STUDENT")) {
            List<Report> reports = reportRepository.findByCreatedBy_Id(jwtUtil.extractAccountIdFromHeader(authHeader));
            return ApiResponse.success(reportMapper.toDTOs(reports));

        }
        List<Report> reports = reportRepository.findAll();
        return ApiResponse.success(reportMapper.toDTOs(reports));

    }

    public long count() {
        return reportRepository.count();
    }

    public ApiResponse<Void> updateReportStatus(Integer id, String status) {

        Report report = reportRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));

        Report.ReportStatus newStatus = Report.ReportStatus.valueOf(status);
        report.setStatus(newStatus);
        reportRepository.save(report);
        return ApiResponse.success("Report updated successfully");

    }

    public ApiResponse<Void> deleteReport(Integer id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REPORT_NOT_FOUND));
        reportRepository.delete(report);
        return ApiResponse.success("Report deleted successfully");
    }
}
