package com.domhub.api.service;

import com.domhub.api.model.Room;
import com.domhub.api.repository.BlockRepository;
import com.domhub.api.repository.RoomRepository;
import com.domhub.api.repository.TypeRoomRepository;
import com.domhub.api.service.RoomRentalService;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.RoomDTO;

import java.lang.reflect.Type;
import java.util.Optional;  // Đảm bảo bạn có import này


import java.util.List;
import java.util.stream.Collectors;


import java.util.List;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomRentalService roomRentalService;
    private final BlockRepository blockRepository;
    private final TypeRoomRepository typeRoomRepository;

    // Constructor injection
    public RoomService(RoomRepository roomRepository, RoomRentalService roomRentalService, BlockRepository blockRepository, TypeRoomRepository typeRoomRepository) {
        this.roomRepository = roomRepository;
        this.roomRentalService = roomRentalService;
        this.blockRepository = blockRepository;
        this.typeRoomRepository = typeRoomRepository;
    }


    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return rooms.stream().map(room -> {
            RoomDTO roomDTO = new RoomDTO();
            roomDTO.setRoomId(room.getId());
            roomDTO.setPrice(room.getPrice());
            roomDTO.setBlockType(blockRepository.findById(room.getBlockId())
                    .orElseThrow(() -> new RuntimeException("Block not found for ID: " + room.getBlockId()))
                    .getType().toString()
            );
            roomDTO.setAvailableBeds(room.getTotalBeds() - roomRentalService.countByRoomId(room.getId()) );
            roomDTO.setTotalBeds(room.getTotalBeds());
            roomDTO.setTypeRoom(typeRoomRepository.findById(room.getType())
                    .orElseThrow(() -> new RuntimeException("Type Room not found for ID: " + room.getType()))
                    .getName()
            );

            return roomDTO;
        }).collect(Collectors.toList());


    }
}