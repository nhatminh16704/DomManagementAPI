package com.domhub.api.dto.request;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationPeriodRequest {
    private Long creator;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
