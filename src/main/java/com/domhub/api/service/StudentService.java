package com.domhub.api.service;

import com.domhub.api.model.Student;
import com.domhub.api.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class StudentService {
    private final StudentRepository studentRepository;


    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }



}
