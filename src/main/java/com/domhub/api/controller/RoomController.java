package com.domhub.api.controller;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.dto.response.RoomDTO;
import com.domhub.api.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.dto.response.RoomDetailDTO;


import java.util.List;

@RestController
@PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
@RequestMapping("/rooms")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/get-all")
    public ApiResponse<List<RoomDTO>> findAll() {
        return roomService.getAllRooms();
    }

    @GetMapping("/{id}")
    public ApiResponse<RoomDetailDTO> getRoomDetail(@PathVariable("id") Integer id) {
        return roomService.getRoomDetail(id);
    }


}
