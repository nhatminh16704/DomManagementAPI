package com.domhub.api.model;


import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "room")
@Getter
@Setter
public class Room {

    @Id
    private String id;

    @Column(nullable = false)
    private String blockId;

    @Column(nullable = false)
    private int type;

    private long price;

    private int totalBeds;
}
