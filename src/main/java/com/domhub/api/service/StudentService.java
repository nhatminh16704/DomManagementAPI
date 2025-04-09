package com.domhub.api.service;

import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.model.Student;
import com.domhub.api.repository.StudentRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.mapper.StudentMapper;
import com.domhub.api.model.Account;
import com.domhub.api.dto.request.AccountRequest;
import com.domhub.api.security.JwtUtil;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final AccountService accountService;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }
    

    public Student getStudentById(Integer id) {
        String authHeader = request.getHeader("Authorization");
        if(jwtUtil.extractRole(authHeader.substring(7)).equals("STUDENT") ){
            
            return studentRepository.findByAccountId(jwtUtil.extractAccountId(authHeader.substring(7))).orElseThrow(() -> new RuntimeException("Student not found with id " + id));
        }else{
            return studentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with id " + id));
        }  
    }

    public Integer getStudentIdByAccountId(Integer accountId) {
        return studentRepository.findStudentIdByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Student not found with account id " + accountId));
    }

    public Student getStudentByAccountId(Integer accountId) {
        return studentRepository.findByAccountId(accountId).orElse(null);
    }

    public Student createStudent(Student student) {
        if (student.getStudentCode() == null || student.getStudentCode().isEmpty()) {
            throw new RuntimeException("Student code cannot be null or empty");
        }
        // Kiểm tra xem studentCode đã tồn tại chưa
        if (studentRepository.existsByStudentCode(student.getStudentCode())) {
            throw new RuntimeException("Student with studentCode " + student.getStudentCode() + " already exists");
        }

        String username = student.getStudentCode();
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setUserName(username);
        accountRequest.setPassword("123456");  // Default password
        accountRequest.setRole("STUDENT");     // Default role

        Account account = accountService.createAccount(accountRequest);

        student.setAccountId(account.getId());
        return studentRepository.save(student);
    }

    public String updateStudent(Integer id, Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isEmpty()) {
            return "Student not found!";
        }

        Student student = optionalStudent.get();

        // Check if the new studentCode is unique before updating
        if (!updatedStudent.getStudentCode().equals(student.getStudentCode()) &&
                studentRepository.existsByStudentCode(updatedStudent.getStudentCode())) {
            throw new RuntimeException("Student with studentCode " + updatedStudent.getStudentCode() + " already exists");
        }
        student.setStudentCode(updatedStudent.getStudentCode());
        student.setFullName(updatedStudent.getFullName());
        student.setEmail(updatedStudent.getEmail());
        student.setPhoneNumber(updatedStudent.getPhoneNumber());
        student.setBirthday(updatedStudent.getBirthday());
        student.setGender(updatedStudent.getGender());
        student.setClassName(updatedStudent.getClassName());
        student.setHometown(updatedStudent.getHometown());

        studentRepository.save(student);
        return "Updated student with id " + id;
    }

    public void deleteStudent(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id " + id));

        // Delete associated account first
        if (student.getAccountId() != null) {
            accountService.deleteAccount(student.getAccountId());
        }

        // Delete the student
        studentRepository.deleteById(id);
    }


}
