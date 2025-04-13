package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.domhub.api.model.DeviceRoom;
import com.domhub.api.model.DeviceRoomId;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import com.domhub.api.dto.response.DeviceRoomDTO;

public interface DeviceRoomRepository extends JpaRepository<DeviceRoom, DeviceRoomId> {
    @Query("SELECT new com.domhub.api.dto.response.DeviceRoomDTO(d.id, d.deviceName, dr.quantity) " +
            "FROM DeviceRoom dr " +
            "JOIN Device d ON dr.id.deviceId = d.id " +
            "WHERE dr.id.roomId = :roomId")
    List<DeviceRoomDTO> findDevicesByRoomId(@Param("roomId") Integer roomId);

}

