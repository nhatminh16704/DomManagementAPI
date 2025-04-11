package com.domhub.api.service;

import com.domhub.api.dto.request.ViolationRequest;
import com.domhub.api.dto.response.ViolationDTO;
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

    public List<ViolationDTO> getAllViolationsByStudentId(Integer studentId) {
        return violationRepository.findAllByStudentId(studentId);
    }

    public Violation createViolation(ViolationRequest violationRequest) {
        String authHeader = httpServletRequest.getHeader("Authorization");

        Violation violation = new Violation();
        violation.setStaffId(staffService.getStaffIdByAccountId(jwtUtil.extractAccountIdFromHeader(authHeader)));
        violation.setStudentId(violationRequest.getStudentId());
        violation.setViolationType(violationRequest.getViolationType());
        violation.setReportDate(violationRequest.getReportDate());
        return violationRepository.save(violation);
    }
}
