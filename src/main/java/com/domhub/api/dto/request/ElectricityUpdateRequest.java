package com.domhub.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ElectricityUpdateRequest {

    @NotNull(message = "Room ID is required")
    private Integer roomId;

    @NotNull(message = "Bill month is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate billMonth;

    @Min(value = 0, message = "Electricity reading must be a positive number")
    private int newEnd;
}

