package com.domhub.api.service;

import com.domhub.api.dto.response.RoomRentalDTO;
import com.domhub.api.model.RoomRental;
import com.domhub.api.model.Student;
import com.domhub.api.repository.RoomRentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.request.RoomRentalRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomRentalService {
    private final RoomService roomService;
    private final StudentService studentService;
    private final RoomRentalRepository roomRentalRepository;


    public boolean canRentRoom(Integer studentId) {
        List<RoomRental> rentals = roomRentalRepository.findByStudentId(studentId);

        for (RoomRental rental : rentals) {
            if ("ACTIVE".equalsIgnoreCase(rental.getStatus().toString()) || "PENDING".equalsIgnoreCase(rental.getStatus().toString()) || "UNPAID".equalsIgnoreCase(rental.getStatus().toString())) {
                return false; // Student already has an active or pending rental
            }
        }
        return true;
    }

    public Integer registerRoomRental(RoomRentalRequest request) {
        Integer studentId = studentService.getStudentByAccountId(request.getAccountId()).getId();

        if (!canRentRoom(studentId)) {
            throw new RuntimeException("You can't rent more rooms");
        }

        RoomRental rental = new RoomRental();
        rental.setRoomId(request.getRoomId());
        rental.setStudentId(studentId);
        rental.setStatus(RoomRental.Status.UNPAID);
        LocalDate today = LocalDate.now();
        int currentYear = LocalDate.now().getYear();
        if(today.getMonthValue()<6){
            rental.setStartDate(LocalDate.of(currentYear, 1, 1));
            rental.setEndDate(LocalDate.of(currentYear, 6, 1));
        }else{
            rental.setStartDate(LocalDate.of(currentYear, 6, 1));
            rental.setEndDate(LocalDate.of(currentYear, 12, 1));
        }
        rental.setPrice(request.getPrice());

        roomRentalRepository.save(rental);

        return rental.getId();
    }

    public List<RoomRentalDTO> getAllRoomRentalsByStudentId(Integer studentId) {
        List<RoomRental> rentals = roomRentalRepository.findByStudentId(studentId);
        return rentals.stream()
                .map(rental -> {
                    var room = roomService.getRoomById(rental.getRoomId());
                    return new RoomRentalDTO(
                            rental.getId(),
                            rental.getStartDate(),
                            rental.getEndDate(),
                            rental.getPrice(),
                            rental.getStatus().toString(),
                            room.getRoomName(),
                            room.getTypeRoom().getName()
                    );
                })
                .toList();
    }



}