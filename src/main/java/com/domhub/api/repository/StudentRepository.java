package com.domhub.api.repository;

import com.domhub.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByAccountId(Integer accountId);
    boolean existsByStudentCode(String studentCode);

    Optional<Integer> findStudentIdByAccountId(Integer accountId);
}
