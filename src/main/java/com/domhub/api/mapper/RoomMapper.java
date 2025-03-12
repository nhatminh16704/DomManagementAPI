package com.domhub.api.mapper;

import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.model.Room;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    public RoomDTO toDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomName(room.getRoomName());
        roomDTO.setPrice(room.getPrice());

        // Lấy block type trực tiếp từ quan hệ
        roomDTO.setBlockType(room.getBlock().getType().toString());

        roomDTO.setAvailable(room.getAvailable());

        roomDTO.setMaxStudents(room.getMaxStudents());

        // Lấy type room name trực tiếp từ quan hệ
        roomDTO.setTypeRoom(room.getTypeRoom().getName());

        return roomDTO;
    }

    public List<RoomDTO> toDTOList(List<Room> rooms) {
        return rooms.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
