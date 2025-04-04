package com.domhub.api.mapper;

import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.model.Room;
import com.domhub.api.model.Student;
import com.domhub.api.repository.RoomRentalRepository;
import org.springframework.stereotype.Component;
import com.domhub.api.dto.response.RoomDetailDTO;
import com.domhub.api.dto.response.DeviceRoomDTO;
import com.domhub.api.repository.DeviceRoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    private final DeviceRoomRepository deviceRoomRepository;
    private final RoomRentalRepository roomRentalRepository;

    public RoomMapper(DeviceRoomRepository deviceRoomRepository, RoomRentalRepository roomRentalRepository) {
        this.deviceRoomRepository = deviceRoomRepository;
        this.roomRentalRepository = roomRentalRepository;
    }

    public RoomDetailDTO toRoomDetailDTO(Room room) {
        RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
        roomDetailDTO.setRoom(toDTO(room));

        List<DeviceRoomDTO> deviceDTOs = deviceRoomRepository.findDevicesByRoomId(room.getId());
        List<Student> students = roomRentalRepository.findStudentsByRoomId(room.getId());

        roomDetailDTO.setStudents(students);
        roomDetailDTO.setDevices(deviceDTOs);

        return roomDetailDTO;
    }



    public RoomDTO toDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomName(room.getRoomName());
        roomDTO.setPrice(room.getPrice());
        roomDTO.setBlockType(room.getBlock().getType().toString());
        roomDTO.setAvailable(room.getAvailable());
        roomDTO.setMaxStudents(room.getMaxStudents());
        roomDTO.setTypeRoom(room.getTypeRoom().getName());

        return roomDTO;
    }

    public List<RoomDTO> toDTOList(List<Room> rooms) {
        return rooms.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
