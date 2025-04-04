package com.domhub.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomRentalRequest {
    private Integer roomId;
    private Double price;
    private Integer accountId;
}
