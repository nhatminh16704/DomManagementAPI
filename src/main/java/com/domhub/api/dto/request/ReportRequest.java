package com.domhub.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportRequest {
    private String title;
    private String content;
    private String sentDate;
    private String status;
}
