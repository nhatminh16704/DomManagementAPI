package com.domhub.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DashboardDTO {
    private Long reportCount;
    private Long roomCount;
    private Long studentCount;
    private Long staffCount;
    private Double revenue;

    private Long totalRoomCapacity;
    private Long availableRoomCount;

}
