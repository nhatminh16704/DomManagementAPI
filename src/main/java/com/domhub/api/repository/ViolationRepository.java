package com.domhub.api.repository;

import com.domhub.api.dto.response.ViolationDTO;
import com.domhub.api.model.Violation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViolationRepository  extends JpaRepository<Violation, Integer> {
    List<ViolationDTO> findAllByStudentId(Integer studentId);
}
