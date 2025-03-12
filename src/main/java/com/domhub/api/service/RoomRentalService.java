package com.domhub.api.service;

import com.domhub.api.model.RoomRental;
import com.domhub.api.repository.RoomRentalRepository;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.request.RoomRentalRequest;


import java.util.List;

@Service
public class RoomRentalService {
    private final RoomService roomService;
    private final StudentService studentService;
    private final RoomRentalRepository roomRentalRepository;

    public RoomRentalService(RoomService roomService, StudentService studentService, RoomRentalRepository roomRentalRepository) {
        this.roomService = roomService;
        this.studentService = studentService;
        this.roomRentalRepository = roomRentalRepository;
    }

    public boolean canRentRoom(Integer studentId) {
        List<RoomRental> rentals = roomRentalRepository.findByStudentId(studentId);

        for (RoomRental rental : rentals) {
            if ("ACTIVE".equalsIgnoreCase(rental.getStatus().toString()) || "PENDING".equalsIgnoreCase(rental.getStatus().toString()) || "UNPAID".equalsIgnoreCase(rental.getStatus().toString())) {
                return false; // Student already has an active or pending rental
            }
        }
        return true;
    }

    public String registerRoomRental(RoomRentalRequest request) {
        Integer studentId = studentService.getStudentByAccountId(request.getAccountId()).getId();

        if (!canRentRoom(studentId)) {
            throw new RuntimeException("You can't rent more rooms");
        }

        RoomRental rental = new RoomRental();
        rental.setRoomId(request.getRoomId());
        rental.setStudentId(studentId);
        rental.setStatus(RoomRental.Status.UNPAID);
        rental.setStartDate(request.getStartDate());
        rental.setEndDate(request.getEndDate());
        rental.setPrice(request.getPrice());

        roomRentalRepository.save(rental);

        return "Room rental created successfully";
    }




}