package com.domhub.api.controller;

import com.domhub.api.dto.request.ChangePasswordRequest;
import com.domhub.api.dto.request.StudentRequest;
import com.domhub.api.dto.request.UpdateProfileRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.model.Student;
import com.domhub.api.service.StudentDetailService;
import com.domhub.api.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Validated
public class StudentController {

    private final StudentService studentService;
    private final StudentDetailService studentDetailService;


    @GetMapping
    public ApiResponse<List<Student>> findAll() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ApiResponse<StudentDTO> getStudentDetailById(@PathVariable @Min(1) Integer id) {
        return studentDetailService.getStudentDetail(id);
    }

    @GetMapping("/info")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Student> getStudentByAccountIdFromStudent() {
        return studentService.getStudentByAccountIdFromStudent();
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<StudentDTO> getStudentProfile() {
        return studentDetailService.getStudentProfileByAccountId();
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Void> updateStudentProfile(@RequestBody @Valid UpdateProfileRequest updateProfileRequest) {
        return studentService.updateProfile(updateProfileRequest);
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> createStudent(@RequestBody @Valid StudentRequest studentRequest) {
        return studentService.createStudent(studentRequest);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> updateStudent(@PathVariable Integer id, @RequestBody @Valid StudentRequest studentRequest) {
        return studentService.updateStudent(id, studentRequest);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteStudent(@PathVariable @Min(1) Integer id) {
        return studentService.deleteStudent(id);
    }


}
