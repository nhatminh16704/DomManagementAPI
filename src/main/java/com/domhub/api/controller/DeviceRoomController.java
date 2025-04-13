package com.domhub.api.controller;


import com.domhub.api.dto.request.DeviceRoomRequest;
import com.domhub.api.service.DeviceRoomService;
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
    public ResponseEntity<String> updateDeviceQuantityInRoom(@RequestBody DeviceRoomRequest deviceRoomRequest) {
        try {
            String result = deviceRoomService.updateQuantity(deviceRoomRequest);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating device quantity: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteDeviceFromRoom(@RequestBody DeviceRoomRequest deviceRoomRequest) {
        try {
            deviceRoomService.deleteDeviceFromRoom(deviceRoomRequest.getRoomId(), deviceRoomRequest.getDeviceId());
            return ResponseEntity.ok("Device successfully removed from room");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error deleting device from room: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addDeviceToRoom(@RequestBody DeviceRoomRequest deviceRoomRequest) {
        try {
            deviceRoomService.addDeviceToRoom(deviceRoomRequest);
            return ResponseEntity.ok("Device successfully added to room");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding device to room: " + e.getMessage());
        }
    }


}
