package com.domhub.api.mapper;

import com.domhub.api.dto.response.RoomRentalDTO;
import com.domhub.api.model.Room;
import com.domhub.api.model.RoomRental;
import com.domhub.api.repository.RoomRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomRentalMapper2 {
    private final RoomRepository roomRepository;

    public RoomRentalMapper2(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public RoomRentalDTO toDTO(RoomRental roomRental) {
        RoomRentalDTO roomRentalDTO = new RoomRentalDTO();
        roomRentalDTO.setId(roomRental.getId());
        roomRentalDTO.setStatus(roomRental.getStatus().toString());
        roomRentalDTO.setStartDate(roomRental.getStartDate());
        roomRentalDTO.setEndDate(roomRental.getEndDate());

        Room room = roomRepository.findById(roomRental.getRoom().getId()).orElseThrow(() -> new RuntimeException("Room not found"));
        roomRentalDTO.setRoomName(room.getRoomName());
        roomRentalDTO.setRoomType(room.getTypeRoom().getName());
        roomRentalDTO.setPrice(roomRental.getPrice());

        return roomRentalDTO;
    }

    public List<RoomRentalDTO> toDTOList(List<RoomRental> roomRentals) {
        return roomRentals.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
