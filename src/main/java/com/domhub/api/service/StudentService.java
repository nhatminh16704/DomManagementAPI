package com.domhub.api.service;

import com.domhub.api.dto.response.StudentDTO;
import com.domhub.api.model.Student;
import com.domhub.api.repository.StudentRepository;
import org.springframework.stereotype.Service;
import com.domhub.api.mapper.StudentMapper;


import java.util.List;


@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;


    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

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


}
