package com.domhub.api.repository;

import com.domhub.api.dto.response.RoomBillDTO;
import com.domhub.api.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT SUM(r.maxStudents) FROM Room r")
    Long sumByMaxStudents();

    @Query("SELECT SUM(r.available) FROM Room r")
    Long sumByAvailable();

    @Query("SELECT r.id FROM Room r")
    List<Integer> findAllRoomIds();



}
