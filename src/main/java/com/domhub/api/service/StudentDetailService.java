package com.domhub.api.service;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.mapper.StudentMapper;
import com.domhub.api.model.Student;
import com.domhub.api.repository.StudentRepository;
import com.domhub.api.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentDetailService {

    private final HttpServletRequest request;
    private final RoomRentalService roomRentalService;
    private final ViolationService violationService;
    private final StudentMapper studentMapper;
    private final StudentRepository studentRepository;
    private final JwtUtil jwtUtil;
    private final AccountService accountService;


    public ApiResponse<StudentDTO> getStudentDetail(Integer studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));
        StudentDTO dto = studentMapper.toDTO(student);
        dto.setRoomRentals(roomRentalService.getAllRoomRentalsByStudentId(studentId).getData());
        dto.setViolations(violationService.getAllViolationsByStudentId(studentId));
        return ApiResponse.success(dto);
    }

    public ApiResponse<StudentDTO> getStudentProfileByAccountId() {
        String authHeader = request.getHeader("Authorization");
        Integer accountId = jwtUtil.extractAccountIdFromHeader(authHeader);
        accountService.validateAccountExists(accountId);
        Student student = studentRepository.findByAccountId(accountId)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND_WITH_ACCOUNT_ID));

        StudentDTO dto = studentMapper.toDTO(student);
        dto.setRoomRentals(roomRentalService.getAllRoomRentalsByStudentId(student.getId()).getData());
        dto.setViolations(violationService.getAllViolationsByStudentId(student.getId()));
        return ApiResponse.success(dto);
    }
}
