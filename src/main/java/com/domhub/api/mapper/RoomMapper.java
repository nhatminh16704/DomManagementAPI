package com.domhub.api.mapper;

import com.domhub.api.dto.RoomDTO;
import com.domhub.api.model.Room;
import com.domhub.api.service.RoomRentalService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {
    private final RoomRentalService roomRentalService;

    public RoomMapper(RoomRentalService roomRentalService) {
        this.roomRentalService = roomRentalService;
    }

    public RoomDTO toDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setRoomId(room.getId());
        roomDTO.setPrice(room.getPrice());

        // Lấy block type trực tiếp từ quan hệ
        roomDTO.setBlockType(room.getBlock().getType().toString());

        // Số giường còn trống
        roomDTO.setAvailableBeds(room.getTotalBeds() - roomRentalService.countByRoomId(room.getId()));
        roomDTO.setTotalBeds(room.getTotalBeds());

        // Lấy type room name trực tiếp từ quan hệ
        roomDTO.setTypeRoom(room.getTypeRoom().getName());

        return roomDTO;
    }

    public List<RoomDTO> toDTOList(List<Room> rooms) {
        return rooms.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
