package com.domhub.api.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
public class ViolationRequest {
    @NotNull(message = "Student ID cannot be null")
    private Integer studentId;

    @NotEmpty(message = "Violation type cannot be empty")
    @Size(max = 255, message = "Violation type must be less than 255 characters")
    private String violationType;

    @NotNull(message = "Report date cannot be null")
    @PastOrPresent(message = "Report date cannot be in the future")
    private LocalDate reportDate;
}
