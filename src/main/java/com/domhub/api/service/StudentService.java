package com.domhub.api.service;

import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.model.Student;
import com.domhub.api.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.mapper.StudentMapper;
import com.domhub.api.model.Account;
import com.domhub.api.dto.request.AccountRequest;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final AccountService accountService;


    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public StudentDTO getStudentById(Integer id) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            StudentDTO studentDTO = studentMapper.toDTO(student);
            return studentDTO;
        }
        return null;
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


}
