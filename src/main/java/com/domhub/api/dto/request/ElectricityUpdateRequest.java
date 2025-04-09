package com.domhub.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ElectricityUpdateRequest {
    private Integer roomId;
    private LocalDate billMonth;
    private int newEnd;
}

