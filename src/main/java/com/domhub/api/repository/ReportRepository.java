package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.domhub.api.model.Report;

public interface ReportRepository extends JpaRepository<Report, Integer> {

}
