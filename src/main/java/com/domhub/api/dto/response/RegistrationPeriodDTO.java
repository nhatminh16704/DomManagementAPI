package com.domhub.api.dto.response;

import java.time.LocalDateTime;

import com.domhub.api.model.RegistrationPeriod.RegistrationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegistrationPeriodDTO {
    private Long id;
    private String namecreator;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private RegistrationStatus isActive;
}
