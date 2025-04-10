package com.domhub.api.repository;

import com.domhub.api.model.Room;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("SELECT SUM(r.maxStudents) FROM Room r")
    Long sumByMaxStudents();

    @Query("SELECT SUM(r.available) FROM Room r")
    Long sumByAvailable();

    @Query("SELECT r.id FROM Room r")
    List<Integer> findAllRoomIds();
    
    List<Room> findByRoomNameContainingIgnoreCase(String key);
}
