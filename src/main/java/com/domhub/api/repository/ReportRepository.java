package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.domhub.api.model.Report;

import java.util.List;

public interface ReportRepository extends JpaRepository<Report, Integer> {
    List<Report> findByCreatedBy_Id(Integer accountId);
}
