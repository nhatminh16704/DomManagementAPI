package com.domhub.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceRoomRequest {

    @NotNull(message = "Room ID cannot be null")
    private Integer roomId;

    @NotNull(message = "Device ID cannot be null")
    private Long deviceId;

    @NotNull(message = "New quantity cannot be null")
    @Min(value = 1, message = "New quantity must be at least 1")
    private int newQuantity;

}

