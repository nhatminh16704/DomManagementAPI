package com.domhub.api.dto.request;


import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReportStatusUpdateRequest {

    @Pattern(regexp = "PENDING|IN_PROGRESS|RESOLVED", message = "Status must be one of: PENDING, IN_PROGRESS, RESOLVED")
    private String status;
}
