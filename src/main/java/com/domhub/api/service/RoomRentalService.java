package com.domhub.api.service;

import com.domhub.api.model.RoomRental;
import com.domhub.api.repository.RoomRentalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomRentalService {
    private final RoomRentalRepository roomRentalRepository;

    public RoomRentalService(RoomRentalRepository roomRentalRepository) {
        this.roomRentalRepository = roomRentalRepository;
    }

    public List<RoomRental> getAllRoomRentals() {
        return roomRentalRepository.findAll();
    }

    public int countByRoomId(String roomId) {
        return roomRentalRepository.countByRoomId(roomId);
    }
}