package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.domhub.api.model.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
