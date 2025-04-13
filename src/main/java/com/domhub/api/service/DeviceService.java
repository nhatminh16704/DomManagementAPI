package com.domhub.api.service;

import com.domhub.api.model.Device;
import com.domhub.api.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository deviceRepository;

    public List<Device> getAllDevices() {
        return deviceRepository.findAll();
    }
}
