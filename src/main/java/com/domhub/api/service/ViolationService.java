package com.domhub.api.service;

import com.domhub.api.dto.request.ViolationRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.model.Account;
import com.domhub.api.model.Violation;
import com.domhub.api.repository.ViolationRepository;
import com.domhub.api.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ViolationService {
    private final ViolationRepository violationRepository;
    private final HttpServletRequest httpServletRequest;
    private final StaffService staffService;
    private final JwtUtil jwtUtil;
    private final StudentService studentService;
    private final AccountService accountService;

    public ApiResponse<List<Violation>> getAllViolationsByStudentId(Integer studentId) {
        if(!studentService.existsById(studentId)) {
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }

        return ApiResponse.success(violationRepository.findAllByStudentId(studentId));
    }

    public ApiResponse<Void> createViolation(ViolationRequest violationRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");
        Integer accountId = jwtUtil.extractAccountIdFromHeader(authHeader);
        accountService.validateAccountExists(accountId, "Account staff not found");

        Violation violation = new Violation();
        violation.setReportedBy(accountId);
        violation.setStudentId(violationRequest.getStudentId());
        violation.setViolationType(violationRequest.getViolationType());
        violation.setReportDate(violationRequest.getReportDate());
        violationRepository.save(violation);
        return ApiResponse.success("Violation created successfully");
    }
}
