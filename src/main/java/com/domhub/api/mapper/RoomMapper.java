package com.domhub.api.mapper;

import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.model.Room;
import org.springframework.stereotype.Component;
import com.domhub.api.dto.response.RoomDetailDTO;
import com.domhub.api.dto.response.DeviceRoomDTO;
import com.domhub.api.repository.DeviceRoomRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RoomMapper {

    private final DeviceRoomRepository deviceRoomRepository;
    public RoomMapper(DeviceRoomRepository deviceRoomRepository) {
        this.deviceRoomRepository = deviceRoomRepository;
    }

    public RoomDetailDTO toRoomDetailDTO(Room room) {
        RoomDetailDTO roomDetailDTO = new RoomDetailDTO();
        roomDetailDTO.setRoom(toDTO(room));
        roomDetailDTO.setDescription(room.getTypeRoom().getDescription());

        // Lấy danh sách thiết bị từ database
        List<DeviceRoomDTO> deviceDTOs = deviceRoomRepository.findByIdRoomId(room.getId()).stream()
                .map(deviceRoom -> new DeviceRoomDTO(
                        deviceRoom.getDevice().getDeviceName(),
                        deviceRoom.getQuantity()))
                .collect(Collectors.toList());

        roomDetailDTO.setDevices(deviceDTOs);

        return roomDetailDTO;
    }



    public RoomDTO toDTO(Room room) {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setRoomName(room.getRoomName());
        roomDTO.setPrice(room.getPrice());

        // Lấy block type trực tiếp từ quan hệ
        roomDTO.setBlockType(room.getBlock().getType().toString());

        roomDTO.setAvailable(room.getAvailable());

        roomDTO.setMaxStudents(room.getMaxStudents());

        // Lấy type room name trực tiếp từ quan hệ
        roomDTO.setTypeRoom(room.getTypeRoom().getName());

        return roomDTO;
    }

    public List<RoomDTO> toDTOList(List<Room> rooms) {
        return rooms.stream().map(this::toDTO).collect(Collectors.toList());
    }

}
