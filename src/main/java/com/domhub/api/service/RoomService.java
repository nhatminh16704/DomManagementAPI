package com.domhub.api.service;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.DeviceRoomDTO;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.mapper.RoomMapper;
import com.domhub.api.model.Room;
import com.domhub.api.model.Student;
import com.domhub.api.repository.DeviceRoomRepository;
import com.domhub.api.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.model.RoomRental;
import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.dto.response.RoomDetailDTO;


import java.util.List;


@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final DeviceRoomRepository deviceRoomRepository;
    private final StudentService studentService;
    private final RoomRentalService roomRentalService;


    public long count() {
        return roomRepository.count();
    }

    public long countCapacity() {
        return roomRepository.sumByMaxStudents();
    }

    public long countAvailable() {
        return roomRepository.sumByAvailable();
    }

    public boolean existsById(Integer roomId) {
        return roomRepository.existsById(roomId);
    }


    public ApiResponse<List<RoomDTO>> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return ApiResponse.success(roomMapper.toDTOs(rooms));
    }

    public void reserveRoom(Integer rentalId) {

        RoomRental rental = roomRentalService.getRoomRentalById(rentalId);

        if (rental.getStatus().equals(RoomRental.Status.PENDING)) {
            throw new AppException(ErrorCode.ROOM_RENTAL_NOT_VALID_SATUS, "Room rental is in PENDING state.");
        }
        if (rental.getStatus().equals(RoomRental.Status.ACTIVE)) {
            throw new AppException(ErrorCode.ROOM_RENTAL_NOT_VALID_SATUS, "Room rental is in ACTIVE state.");
        }
        if (rental.getStatus().equals(RoomRental.Status.EXPIRED)) {
            throw new AppException(ErrorCode.ROOM_RENTAL_NOT_VALID_SATUS, "Room rental is in EXPIRED state.");
        }

        Room room = roomRepository.findById(rental.getRoom().getId())
                .orElseThrow(() -> {
                    cancelRoomRental(rentalId);
                    return new AppException(ErrorCode.ROOM_NOT_FOUND);
                });

        if (!studentService.existsById(rental.getStudentId())) {
            cancelRoomRental(rentalId);
            throw new AppException(ErrorCode.STUDENT_NOT_FOUND);
        }


        if (room.getAvailable() <= 0) {
            cancelRoomRental(rentalId);
            throw new AppException(ErrorCode.ROOM_FULL);
        }

        rental.setStatus(RoomRental.Status.PENDING);
        roomRentalService.update(rental);


        room.setAvailable(room.getAvailable() - 1);
        roomRepository.save(room);
    }

    public void cancelRoomRental(Integer rentalId) {
        RoomRental rental = roomRentalService.getRoomRentalById(rentalId);
        Room room = getRoomById(rental.getRoom().getId());

        room.setAvailable(room.getAvailable() + 1);
        roomRepository.save(room);

        roomRentalService.deleteById(rentalId);


    }

    public ApiResponse<RoomDetailDTO> getRoomDetail(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
        return ApiResponse.success(toRoomDetailDTO(room));
    }

    public Room getRoomById(Integer roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_FOUND));
    }


    public RoomDetailDTO toRoomDetailDTO(Room room) {
        RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
        roomDetailDTO.setRoom(roomMapper.toDTO(room));
        List<DeviceRoomDTO> deviceDTOs = deviceRoomRepository.findDevicesByRoomId(room.getId());
        List<Student> students = roomRentalService.getStudentsByRoomId(room.getId());
        roomDetailDTO.setStudents(students);
        roomDetailDTO.setDevices(deviceDTOs);
        return roomDetailDTO;
    }

}
