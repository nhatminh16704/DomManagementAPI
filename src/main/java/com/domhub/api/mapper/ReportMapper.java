package com.domhub.api.mapper;


import com.domhub.api.dto.request.ReportRequest;
import com.domhub.api.dto.response.ReportDTO;
import com.domhub.api.model.Report;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = AccountMapper.class)
public interface ReportMapper {

    @Mapping(target = "status", expression = "java(report.getStatus().name())")
    ReportDTO toDTO(Report report);

    List<ReportDTO> toDTOs(List<Report> reports);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "sentDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "status", expression = "java(Report.ReportStatus.PENDING)")
    Report toEntity(ReportRequest request);



}
