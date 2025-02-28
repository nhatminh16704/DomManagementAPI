package com.domhub.api.repository;

import com.domhub.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByStudentId(String studentId);
}
