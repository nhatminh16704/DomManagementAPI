package com.domhub.api.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.FutureOrPresent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationPeriodRequest {
    @NotNull(message = "Creator ID is required")
    private Long creator;

    @NotBlank(message = "Registration period name is required")
    private String name;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in the present or future")
    private LocalDateTime endDate;
}