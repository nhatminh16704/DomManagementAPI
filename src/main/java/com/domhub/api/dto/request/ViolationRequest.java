package com.domhub.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ViolationRequest {
    private Integer studentId;
    private String violationType;
    private LocalDate reportDate;
}
