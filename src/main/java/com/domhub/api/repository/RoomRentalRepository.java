package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.domhub.api.model.RoomRental;
import com.domhub.api.model.Student;

import  java.util.List;

public interface RoomRentalRepository extends JpaRepository<RoomRental, Integer> {
    List<RoomRental> findByStudentId(Integer studentId);
    @Query("SELECT r.studentId FROM RoomRental r WHERE r.roomId = :roomId AND r.status = 'ACTIVE'")
    List<Integer> findActiveRentalsByRoomId(@Param("roomId") Integer roomId);
}
