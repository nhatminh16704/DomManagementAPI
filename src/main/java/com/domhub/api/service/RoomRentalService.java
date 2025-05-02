package com.domhub.api.service;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.RoomRentalDTO;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.mapper.RoomRentalMapper;
import com.domhub.api.model.Account;
import com.domhub.api.model.Room;
import com.domhub.api.model.RoomRental;
import com.domhub.api.model.Student;
import com.domhub.api.repository.RegistrationPeriodRepository;
import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.repository.RoomRepository;
import com.domhub.api.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.request.RoomRentalRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomRentalService {
    private final RoomRepository roomRepository;
    private final StudentService studentService;
    private final RoomRentalRepository roomRentalRepository;
    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final HttpServletRequest httpServletRequest;
    private final JwtUtil jwtUtil;
    private final RoomRentalMapper roomRentalMapper;


    public boolean canRentRoom(Integer studentId) {
        List<RoomRental> rentals = roomRentalRepository.findByStudentId(studentId);

        for (RoomRental rental : rentals) {
            if ("ACTIVE".equalsIgnoreCase(rental.getStatus().toString()) || "PENDING".equalsIgnoreCase(rental.getStatus().toString()) || "UNPAID".equalsIgnoreCase(rental.getStatus().toString())) {
                return false; // Student already has an active or pending rental
            }
        }
        return true;
    }

    public Double getTotalRevenue() {
        return roomRentalRepository.sumByPrice();
    }

    public RoomRental update(RoomRental rental) {
        return roomRentalRepository.save(rental);
    }

    public void deleteById(Integer id) {
        roomRentalRepository.deleteById(id);
    }

    public List<Student> getStudentsByRoomId(Integer roomId) {
        return roomRentalRepository.findStudentsByRoomId(roomId);
    }

    public RoomRental getRoomRentalById(Integer id) {
        return roomRentalRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_RENTAL_NOT_FOUND));
    }

    public ApiResponse<Integer> registerRoomRental(RoomRentalRequest request) {
        String token = httpServletRequest.getHeader("Authorization");
        Integer accountId = jwtUtil.extractAccountIdFromHeader(token);

        Student student = studentService.getStudentByAccountId(accountId);
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));

        if (!student.getGender().name().equals(room.getBlock().getType().name())) {
            throw new AppException(ErrorCode.CANT_REGISTER_ROOM, "Can't register room in this block");
        }

        if (!registrationPeriodRepository.existsByIsActiveTrue()) {
            throw new AppException(ErrorCode.NOT_IN_REGISTRATION_PERIOD);
        }

        if (!canRentRoom(student.getId())) {
            throw new AppException(ErrorCode.CANT_REGISTER_ROOM, "Student already has an active or pending rental");
        }


        RoomRental rental = new RoomRental();
        rental.setStudentId(student.getId());
        LocalDate today = LocalDate.now();
        int currentYear = LocalDate.now().getYear();
        if (today.getMonthValue() < 6) {
            rental.setStartDate(LocalDate.of(currentYear, 1, 1));
            rental.setEndDate(LocalDate.of(currentYear, 6, 1));
        } else {
            rental.setStartDate(LocalDate.of(currentYear, 6, 1));
            rental.setEndDate(LocalDate.of(currentYear, 12, 1));
        }
        rental.setRoom(room);
        rental.setPrice(request.getPrice());
        rental.setStatus(RoomRental.Status.UNPAID);
        RoomRental tmp = roomRentalRepository.save(rental);

        return ApiResponse.success(tmp.getId(), "Room rental created successfully");
    }

    public ApiResponse<List<RoomRentalDTO>> getAllRoomRentalsByStudentId(Integer studentId) {

        List<RoomRental> rentals = roomRentalRepository.findByStudentId(studentId);
        List<RoomRentalDTO> rentalDTOS = roomRentalMapper.toDTOs(rentals);
        return ApiResponse.success(rentalDTOS);

    }

    public Integer getCurrentRoomByStudentId(Integer studentId) {
        return roomRentalRepository.findByStudentIdAndStatus(studentId, RoomRental.Status.ACTIVE)
                .map(rr -> rr.getRoom().getId())
                .orElseThrow(() -> new AppException(ErrorCode.STUDENT_NOT_REGISTERED_ROOM));
    }


}