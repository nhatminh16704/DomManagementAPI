package com.domhub.api.dto.response;

import java.time.LocalDate;

public interface ViolationDTO {
    Integer getId();
    String getViolationType();
    LocalDate getReportDate();
}
