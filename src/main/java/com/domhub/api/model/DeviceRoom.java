package com.domhub.api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device_room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRoom {

    @EmbeddedId
    private DeviceRoomId id;



    @Column(nullable = false)
    private int quantity;
}

