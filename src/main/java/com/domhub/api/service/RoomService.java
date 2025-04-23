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
    private final RoomRentalRepository roomRentalRepository;
    private final RoomMapper roomMapper;
    private final DeviceRoomRepository deviceRoomRepository;


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
        // Tìm đơn thuê phòng
        RoomRental rental = roomRentalRepository.findById(rentalId)
                .orElseThrow(() -> new RuntimeException("Room rental not found"));

        // Kiểm tra đơn thuê phòng có ở trạng thái PENDING không
        if (rental.getStatus().equals(RoomRental.Status.PENDING)) {
            throw new RuntimeException("Room rental is in PENDING state.");
        }
        if (rental.getStatus().equals(RoomRental.Status.ACTIVE)) {
            throw new RuntimeException("Room rental is in ACTIVE state.");
        }
        if (rental.getStatus().equals(RoomRental.Status.EXPIRED)) {
            throw new RuntimeException("Room rental is in EXPIRED state.");
        }

        // Kiểm tra phòng có còn trống không
        Room room = roomRepository.findById(rental.getRoom().getId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        if (room.getAvailable() <= 0) {
            throw new RuntimeException("No available rooms left.");
        }

        rental.setStatus(RoomRental.Status.PENDING);
        roomRentalRepository.save(rental);

        // Cập nhật số lượng phòng trống
        room.setAvailable(room.getAvailable() - 1);
        roomRepository.save(room);
    }

    public void cancelRoomRental(Integer rentalId) {
        RoomRental rental = roomRentalRepository.findById(rentalId).orElse(null);
        if (rental != null) {
            Room room = roomRepository.findById(rental.getRoom().getId()).orElse(null);
            // Tăng số lượng phòng trống lên vì đơn thuê bị hủy
            if (room != null) {
                room.setAvailable(room.getAvailable() + 1);
                roomRepository.save(room);
            }
            // Xóa RoomRental khỏi database
            roomRentalRepository.deleteById(rentalId);
        }

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
        List<Student> students = roomRentalRepository.findStudentsByRoomId(room.getId());
        roomDetailDTO.setStudents(students);
        roomDetailDTO.setDevices(deviceDTOs);
        return roomDetailDTO;
    }

}
