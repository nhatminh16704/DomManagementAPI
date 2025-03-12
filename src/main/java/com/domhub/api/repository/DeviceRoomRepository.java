package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.domhub.api.model.DeviceRoom;
import com.domhub.api.model.DeviceRoomId;
import org.springframework.data.repository.query.Param;

public interface DeviceRoomRepository extends JpaRepository<DeviceRoom, DeviceRoomId> {
    List<DeviceRoom> findByIdRoomId(Integer roomId);

}

