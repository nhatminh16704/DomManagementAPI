package com.domhub.api.service;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.model.Device;
import com.domhub.api.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public ApiResponse<List<Device>> getAllDevices() {
         return ApiResponse.success(deviceRepository.findAll());
    }
}
