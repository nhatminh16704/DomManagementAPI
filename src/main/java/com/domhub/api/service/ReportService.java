package com.domhub.api.service;

import com.domhub.api.model.Report;
import com.domhub.api.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.response.ReportDTO;

import java.util.stream.Collectors;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public String createReport(Report report) {
        if (report.getStatus() == null) {
            report.setStatus(Report.ReportStatus.PENDING);
        }
        reportRepository.save(report);
        return "Report created successfully with ID: " + report.getId();
    }


    public List<ReportDTO> findAllReports() {
        return reportRepository.findAll().stream()
                .map(report -> new ReportDTO(
                        report.getId(),
                        report.getCreatedBy().toAccountDTO(),
                        report.getTitle(),
                        report.getContent(),
                        report.getSentDate(),
                        report.getStatus().name()
                ))
                .collect(Collectors.toList());
    }

    public long count() {
        return reportRepository.count();
    }

}
