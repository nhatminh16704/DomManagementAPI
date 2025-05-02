package com.domhub.api.controller;


import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.model.RoomRental;
import com.domhub.api.service.RoomRentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.domhub.api.dto.request.RoomRentalRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.domhub.api.dto.response.RoomRentalDTO;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/room-rental")
public class RoomRentalController {
    private final RoomRentalService roomRentalService;


    @PostMapping("/register")
    @PreAuthorize("hasRole('STUDENT')")
    public ApiResponse<Integer> createRoomRental(@RequestBody @Valid RoomRentalRequest request) {
            return roomRentalService.registerRoomRental(request);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<RoomRentalDTO>> getAllRoomRentalsByStudentId(@PathVariable Integer studentId) {
            return roomRentalService.getAllRoomRentalsByStudentId(studentId);
    }


}
