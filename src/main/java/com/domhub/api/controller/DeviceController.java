package com.domhub.api.controller;

import com.domhub.api.model.Device;
import com.domhub.api.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }
}
