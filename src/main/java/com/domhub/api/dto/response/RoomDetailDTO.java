package com.domhub.api.dto.response;

import java.util.List;

import com.domhub.api.model.Student;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RoomDetailDTO {
    private RoomDTO room;
    private List<DeviceRoomDTO> devices;
    private List<Student> students;

}
