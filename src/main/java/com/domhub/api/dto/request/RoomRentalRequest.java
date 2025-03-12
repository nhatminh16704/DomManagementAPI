package com.domhub.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRentalRequest {
    private Integer roomId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private Integer accountId;
}
