package com.domhub.api.repository;


import com.domhub.api.dto.response.RoomBillDTO;
import com.domhub.api.model.RoomBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomBillRepository extends JpaRepository<RoomBill, Integer> {


    Optional<RoomBill> findByRoomIdAndBillMonth(Integer roomId, LocalDate billMonth);

    boolean existsByRoomIdAndBillMonth(Integer roomId, LocalDate billMonth);

    @Query("SELECT new com.domhub.api.dto.response.RoomBillDTO(rb.id, rb.roomId, r.roomName, rb.billMonth, " +
            "rb.electricityStart, rb.electricityEnd, rb.totalAmount, rb.status) " +
            "FROM RoomBill rb JOIN Room r ON rb.roomId = r.id")
    List<RoomBillDTO> findAllRoomBills();


    @Query("SELECT new com.domhub.api.dto.response.RoomBillDTO(rb.id, rb.roomId, r.roomName, rb.billMonth, " +
            "rb.electricityStart, rb.electricityEnd, rb.totalAmount, rb.status) " +
            "FROM RoomBill rb JOIN Room r ON rb.roomId = r.id " +
            "WHERE rb.billMonth = :billMonth AND rb.status = :status")
    List<RoomBillDTO> findAllByBillMonthAndStatus(LocalDate billMonth, RoomBill.BillStatus status);

    @Query("SELECT new com.domhub.api.dto.response.RoomBillDTO(rb.id, rb.roomId, r.roomName, rb.billMonth, " +
            "rb.electricityStart, rb.electricityEnd, rb.totalAmount, rb.status) " +
            "FROM RoomBill rb JOIN Room r ON rb.roomId = r.id " +
            "WHERE rb.roomId = :roomId AND rb.status != 'PENDING'")
    List<RoomBillDTO> findAllByRoomId(Integer roomId);



}
