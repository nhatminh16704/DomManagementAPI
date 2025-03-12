package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.domhub.api.model.RoomRental;
import  java.util.List;

public interface RoomRentalRepository extends JpaRepository<RoomRental, Integer> {
    List<RoomRental> findByStudentId(Integer studentId);
}
