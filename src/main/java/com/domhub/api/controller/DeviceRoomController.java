package com.domhub.api.controller;


import com.domhub.api.dto.request.DeviceRoomRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.service.DeviceRoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/device-rooms")
@RequiredArgsConstructor
public class DeviceRoomController {
    private final DeviceRoomService deviceRoomService;

    @PutMapping("/update-quantity")
    public ApiResponse<Void> updateDeviceQuantityInRoom(@RequestBody @Valid DeviceRoomRequest deviceRoomRequest) {
            return  deviceRoomService.updateQuantity(deviceRoomRequest);
    }

    @DeleteMapping("/delete")
    public ApiResponse<Void> deleteDeviceFromRoom(@RequestBody @Valid DeviceRoomRequest deviceRoomRequest) {
            return deviceRoomService.deleteDeviceFromRoom(deviceRoomRequest.getRoomId(), deviceRoomRequest.getDeviceId());

    }

    @PostMapping("/add")
    public ApiResponse<Void> addDeviceToRoom(@RequestBody @Valid DeviceRoomRequest deviceRoomRequest) {
            return deviceRoomService.addDeviceToRoom(deviceRoomRequest);
    }


}
