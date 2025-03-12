package com.domhub.api.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Table(name = "room")
@Getter
@Setter
public class Room {

    @Id
    private Integer id;

    @Column(nullable = false, unique = true)
    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY) // Tạo quan hệ với Block
    @JoinColumn(name = "block_id", referencedColumnName = "id", nullable = false)
    private Block block;

    @ManyToOne(fetch = FetchType.LAZY) // Tạo quan hệ với TypeRoom
    @JoinColumn(name = "type", referencedColumnName = "id", nullable = false)
    private TypeRoom typeRoom;

    private long price;
    private int maxStudents;
    private int available;
}
