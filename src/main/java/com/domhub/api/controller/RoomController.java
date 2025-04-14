package com.domhub.api.controller;

import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.dto.response.RoomDetailDTO;


import java.util.List;

@RestController
@RequestMapping("/rooms")
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoomDetail(@PathVariable("id") Integer id) {
        try {
            RoomDetailDTO roomDetail = roomService.getRoomDetail(id);
            return ResponseEntity.ok(roomDetail); // 200 OK
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage()); // 400 Bad Request
        }
    }




}
