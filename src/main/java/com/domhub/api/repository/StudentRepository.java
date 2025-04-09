package com.domhub.api.repository;

import com.domhub.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByAccountId(Integer accountId);
    boolean existsByStudentCode(String studentCode);

    @Query("SELECT s.id FROM Student s WHERE s.accountId = :accountId")
    Optional<Integer> findStudentIdByAccountId(@Param("accountId") Integer accountId);

}
