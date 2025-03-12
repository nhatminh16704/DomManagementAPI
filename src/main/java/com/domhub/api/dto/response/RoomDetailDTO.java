package com.domhub.api.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomDetailDTO {
    private RoomDTO room;
    private String description;
    private List<DeviceRoomDTO> devices;

}
