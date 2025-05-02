package com.domhub.api.service;

import com.domhub.api.dto.request.StudentRequest;
import com.domhub.api.dto.request.UpdateProfileRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.mapper.StudentMapper;
import com.domhub.api.model.Student;
import com.domhub.api.repository.StudentRepository;
import com.domhub.api.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.mapper.StudentMapper2;
import com.domhub.api.model.Account;
import com.domhub.api.dto.request.AccountRequest;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final AccountService accountService;
    private final HttpServletRequest request;
    private final JwtUtil jwtUtil;
    private final StudentMapper studentMapper;


    public long count() {
        return studentRepository.count();
    }

    public ApiResponse<List<Student>> getAllStudents() {
        return ApiResponse.success(studentRepository.findAll());
    }


    public boolean existsById(Integer id) {
        return studentRepository.existsById(id);
    }




    public ApiResponse<Student> getStudentByAccountIdFromStudent() {
        String authHeader = request.getHeader("Authorization");
        Student student = getStudentByAccountId(jwtUtil.extractAccountIdFromHeader(authHeader));
        return ApiResponse.success(student);
    }


    public Integer getStudentIdByAccountId(Integer accountId) {
        return studentRepository.findStudentIdByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Student not found with account id " + accountId));
    }


    public Student getStudentByAccountId(Integer accountId) {
        return studentRepository.findByAccountId(accountId).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND_WITH_ACCOUNT_ID));
    }


    public ApiResponse<Void> createStudent(StudentRequest studentRequest) {
        if (studentRepository.existsByStudentCode(studentRequest.getStudentCode())) {
            throw new AppException(ErrorCode.STUDENT_CODE_ALREADY_EXISTS);
        }
        if (studentRepository.existsByEmail(studentRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (studentRepository.existsByPhoneNumber(studentRequest.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        Student student = studentMapper.toEntity(studentRequest);

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserName(student.getStudentCode());
        accountRequest.setPassword("123456");  // Default password
        accountRequest.setRole("STUDENT");     // Default role

        Account account = accountService.createAccount(accountRequest);
        student.setAccountId(account.getId());
        studentRepository.save(student);

        return ApiResponse.success("Student created successfully");
    }


    public ApiResponse<Void> updateStudent(Integer id, StudentRequest studentRequest) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        if (studentRepository.existsByStudentCode(studentRequest.getStudentCode()) &&
                !student.getStudentCode().equals(studentRequest.getStudentCode())) {
            throw new AppException(ErrorCode.STUDENT_CODE_ALREADY_EXISTS);
        }
        if (studentRepository.existsByEmail(studentRequest.getEmail()) &&
                !student.getEmail().equals(studentRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }
        if (studentRepository.existsByPhoneNumber(studentRequest.getPhoneNumber()) &&
                !student.getPhoneNumber().equals(studentRequest.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        student.setStudentCode(studentRequest.getStudentCode());
        student.setFullName(studentRequest.getFullName());
        student.setEmail(studentRequest.getEmail());
        student.setPhoneNumber(studentRequest.getPhoneNumber());
        student.setBirthday(studentRequest.getBirthday());
        student.setGender(Student.Gender.valueOf(studentRequest.getGender()));
        student.setClassName(studentRequest.getClassName());
        student.setHometown(studentRequest.getHometown());

        String username = student.getStudentCode();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserName(username);
        accountRequest.setPassword("123456");
        accountRequest.setRole("STUDENT");
        accountService.updateAccount(accountRequest, student.getAccountId());

        studentRepository.save(student);
        return ApiResponse.success("Student updated successfully");
    }

    public ApiResponse<Void> updateProfile(UpdateProfileRequest updateProfileRequest) {
        String authHeader = request.getHeader("Authorization");
        Integer accountId = jwtUtil.extractAccountIdFromHeader(authHeader);
        accountService.validateAccountExists(accountId);
        Student student = studentRepository.findByAccountId(accountId).orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND_WITH_ACCOUNT_ID));

        if (!updateProfileRequest.getEmail().equals(student.getEmail()) &&
                studentRepository.existsByEmail(updateProfileRequest.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (!updateProfileRequest.getPhoneNumber().equals(student.getPhoneNumber()) &&
                studentRepository.existsByPhoneNumber(updateProfileRequest.getPhoneNumber())) {
            throw new AppException(ErrorCode.PHONE_ALREADY_EXISTS);
        }

        student.setEmail(updateProfileRequest.getEmail());
        student.setPhoneNumber(updateProfileRequest.getPhoneNumber());

        studentRepository.save(student);
        return ApiResponse.success("Profile updated successfully");
    }


    public ApiResponse<Void> deleteStudent(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_FOUND));

        // Delete associated account first
        if (student.getAccountId() != null) {
            accountService.deleteAccount(student.getAccountId());
        }
        // Delete the student
        studentRepository.deleteById(id);
        return ApiResponse.success("Student deleted successfully");
    }


}
