package com.domhub.api.service;

import com.domhub.api.mapper.RoomMapper;
import com.domhub.api.model.Room;
import com.domhub.api.repository.RoomRepository;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.RoomDTO;


import java.util.List;


@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    // Constructor injection
    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.toDTOList(rooms);
    }


}
