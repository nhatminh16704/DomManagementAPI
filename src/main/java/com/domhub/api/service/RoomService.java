package com.domhub.api.service;

import com.domhub.api.mapper.RoomMapper;
import com.domhub.api.model.Room;
import com.domhub.api.repository.RoomRepository;
import org.springframework.stereotype.Service;
import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.model.RoomRental;
import com.domhub.api.repository.RoomRentalRepository;
import com.domhub.api.dto.response.RoomDetailDTO;




import java.util.List;


@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomRentalRepository roomRentalRepository;

    public RoomService(RoomRepository roomRepository, RoomMapper roomMapper, RoomRentalRepository roomRentalRepository) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
        this.roomRentalRepository = roomRentalRepository;
    }

    public List<RoomDTO> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        return roomMapper.toDTOList(rooms);
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
        Room room = roomRepository.findById(rental.getRoomId())
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
            Room room = roomRepository.findById(rental.getRoomId()).orElse(null);
            // Tăng số lượng phòng trống lên vì đơn thuê bị hủy
            if (room != null) {
                room.setAvailable(room.getAvailable() + 1);
                roomRepository.save(room);
            }
            // Xóa RoomRental khỏi database
            roomRentalRepository.deleteById(rentalId);
        }

    }

    public RoomDetailDTO getRoomDetail(Integer roomId) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found with id: " + roomId));
        return roomMapper.toRoomDetailDTO(room); // Convert entity -> DTO
    }


}
