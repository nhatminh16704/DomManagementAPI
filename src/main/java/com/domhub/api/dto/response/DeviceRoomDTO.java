package com.domhub.api.dto.response;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
public class DeviceRoomDTO {
    private Long id;
    private String deviceName;
    private int quantity;
}

