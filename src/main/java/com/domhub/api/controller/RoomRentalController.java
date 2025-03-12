package com.domhub.api.controller;


import com.domhub.api.service.RoomRentalService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.domhub.api.dto.request.RoomRentalRequest;


@RestController
@RequestMapping("/roomrental")
public class RoomRentalController {
    private final RoomRentalService roomRentalService;

    public RoomRentalController(RoomRentalService roomRentalService) {
        this.roomRentalService = roomRentalService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<String> createRoomRental(@RequestBody RoomRentalRequest request) {
        try {
            String message = roomRentalService.registerRoomRental(request);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }


}
