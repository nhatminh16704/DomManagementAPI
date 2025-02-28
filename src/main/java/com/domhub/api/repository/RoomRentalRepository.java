package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.domhub.api.model.RoomRental;

public interface RoomRentalRepository extends JpaRepository<RoomRental, Long> {
    int countByRoomId(String roomId);
}
