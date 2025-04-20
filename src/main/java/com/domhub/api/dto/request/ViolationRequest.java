package com.domhub.api.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
public class ViolationRequest {
    private Integer studentId;
    private String violationType;
    private LocalDate reportDate;
}
