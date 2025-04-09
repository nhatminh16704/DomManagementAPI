package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.domhub.api.model.RoomRental;
import com.domhub.api.model.Student;

import  java.util.List;
import java.util.Optional;

public interface RoomRentalRepository extends JpaRepository<RoomRental, Integer> {
    List<RoomRental> findByStudentId(Integer studentId);

    @Query("SELECT s FROM Student s " +
            "JOIN RoomRental r ON r.studentId = s.id " +
            "WHERE r.roomId = :roomId AND r.status = 'ACTIVE'")
    List<Student> findStudentsByRoomId(@Param("roomId") Integer roomId);

    @Query("SELECT SUM(r.price) FROM RoomRental r")
    Double sumByPrice();

    Optional<RoomRental> findByStudentIdAndStatus(Integer studentId, RoomRental.Status status);
}
