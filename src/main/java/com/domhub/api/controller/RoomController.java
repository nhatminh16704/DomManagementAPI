package com.domhub.api.controller;

import com.domhub.api.dto.RoomDTO;
import com.domhub.api.model.Room;
import com.domhub.api.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@CrossOrigin(origins = "*")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/findAll")
    public ResponseEntity<List<RoomDTO>> findAll() {
        List<RoomDTO> rooms = roomService.getAllRooms();
        return ResponseEntity.ok(rooms);
    }
}
