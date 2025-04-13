package com.domhub.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeviceRoomRequest {
    private Integer roomId;
    private Long deviceId;
    private int newQuantity;
}

