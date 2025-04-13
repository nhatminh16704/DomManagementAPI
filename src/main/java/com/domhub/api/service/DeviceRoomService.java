package com.domhub.api.service;


import com.domhub.api.dto.request.DeviceRoomRequest;
import com.domhub.api.model.DeviceRoom;
import com.domhub.api.model.DeviceRoomId;
import com.domhub.api.repository.DeviceRepository;
import com.domhub.api.repository.DeviceRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DeviceRoomService {
    private final DeviceRoomRepository deviceRoomRepository;
    private final DeviceRepository deviceService;

    public String updateQuantity(DeviceRoomRequest deviceRoomRequest) {

        DeviceRoomId deviceRoomId = new DeviceRoomId(deviceRoomRequest.getRoomId(), deviceRoomRequest.getDeviceId());
        DeviceRoom deviceRoom = deviceRoomRepository.findById(deviceRoomId)
                .orElseThrow(() -> new RuntimeException("Device not found in room with roomId: "
                        + deviceRoomRequest.getRoomId() + " and deviceId: " + deviceRoomRequest.getDeviceId()));

        deviceRoom.setQuantity(deviceRoomRequest.getNewQuantity());
        deviceRoomRepository.save(deviceRoom);
        return "Updated quantity successfully";
    }

    public void deleteDeviceFromRoom(Integer roomId, Long deviceId) {
        DeviceRoomId deviceRoomId = new DeviceRoomId(roomId, deviceId);
        if (!deviceRoomRepository.existsById(deviceRoomId)) {
            throw new RuntimeException("Device not found in room with roomId: " + roomId + " and deviceId: " + deviceId);
        }
        deviceRoomRepository.deleteById(deviceRoomId);
    }

    public void addDeviceToRoom(DeviceRoomRequest deviceRoomRequest) {
        System.out.println("Updating device quantity - Room ID: " + deviceRoomRequest.getRoomId() +
                ", Device ID: " + deviceRoomRequest.getDeviceId() +
                ", New Quantity: " + deviceRoomRequest.getNewQuantity());
        DeviceRoomId deviceRoomId = new DeviceRoomId(deviceRoomRequest.getRoomId(), deviceRoomRequest.getDeviceId());
        if (deviceRoomRepository.existsById(deviceRoomId)) {
            throw new RuntimeException("Device already exists in room with roomId: " + deviceRoomRequest.getRoomId()
                    + " and deviceId: " + deviceRoomRequest.getDeviceId());
        }
        DeviceRoom deviceRoom = new DeviceRoom();
        deviceRoom.setId(deviceRoomId);
        deviceRoom.setQuantity(deviceRoomRequest.getNewQuantity());
        deviceRoomRepository.save(deviceRoom);
    }

}
