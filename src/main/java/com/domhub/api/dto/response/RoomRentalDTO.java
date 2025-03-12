package com.domhub.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRentalDTO {
    private Integer id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double price;
    private String status;
    private String roomName;
    private String roomType;
}

