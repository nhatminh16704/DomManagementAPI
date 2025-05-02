package com.domhub.api.repository;

import com.domhub.api.model.Violation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViolationRepository  extends JpaRepository<Violation, Integer> {
    List<Violation> findAllByStudentId(Integer studentId);
}
