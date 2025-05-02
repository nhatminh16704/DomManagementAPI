package com.domhub.api.service;


import com.domhub.api.dto.request.DeviceRoomRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.DeviceRoomDTO;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.model.DeviceRoom;
import com.domhub.api.model.DeviceRoomId;
import com.domhub.api.repository.DeviceRepository;
import com.domhub.api.repository.DeviceRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DeviceRoomService {
    private final DeviceRoomRepository deviceRoomRepository;
    private final DeviceRepository deviceService;
    private final RoomService roomService;

    public ApiResponse<Void> updateQuantity(DeviceRoomRequest deviceRoomRequest) {

        DeviceRoomId deviceRoomId = new DeviceRoomId(deviceRoomRequest.getRoomId(), deviceRoomRequest.getDeviceId());
        DeviceRoom deviceRoom = deviceRoomRepository.findById(deviceRoomId)
                .orElseThrow(() -> new AppException(ErrorCode.DEVICE_NOT_FOUND_IN_ROOM));

        deviceRoom.setQuantity(deviceRoomRequest.getNewQuantity());
        deviceRoomRepository.save(deviceRoom);
        return ApiResponse.success(null);
    }

    public List<DeviceRoomDTO> getDevicesByRoomId(Integer id) {
        return deviceRoomRepository.findDevicesByRoomId(id);
    }

    public ApiResponse<Void> deleteDeviceFromRoom(Integer roomId, Long deviceId) {
        DeviceRoomId deviceRoomId = new DeviceRoomId(roomId, deviceId);
        if (!deviceRoomRepository.existsById(deviceRoomId)) {
            throw new AppException(ErrorCode.DEVICE_NOT_FOUND_IN_ROOM);
        }
        deviceRoomRepository.deleteById(deviceRoomId);
        return ApiResponse.success("Device successfully deleted from room");
    }

    public ApiResponse<Void> addDeviceToRoom(DeviceRoomRequest deviceRoomRequest) {



        if (!deviceService.existsById(deviceRoomRequest.getDeviceId())) {
            throw new AppException(ErrorCode.DEVICE_NOT_FOUND);
        }
        if (!roomService.existsById(deviceRoomRequest.getRoomId())) {
            throw new AppException(ErrorCode.ROOM_NOT_FOUND);
        }

        DeviceRoomId deviceRoomId = new DeviceRoomId(deviceRoomRequest.getRoomId(), deviceRoomRequest.getDeviceId());

        // Check if the device already exists in the room
        if (deviceRoomRepository.existsById(deviceRoomId)) {
            throw new AppException(ErrorCode.DEVICE_ALREADY_EXISTS_IN_ROOM);
        }

        DeviceRoom deviceRoom = new DeviceRoom();
        deviceRoom.setId(deviceRoomId);
        deviceRoom.setQuantity(deviceRoomRequest.getNewQuantity());
        deviceRoomRepository.save(deviceRoom);
        return ApiResponse.success("Device successfully added to room");
    }

}
