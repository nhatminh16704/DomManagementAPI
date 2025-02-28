package com.domhub.api.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "block")
@Getter
@Setter
public class Block {

    @Id
    private String id;

    @Column(nullable = false)

    @Enumerated(EnumType.STRING)
    private TypeRoom type;

    private int totalRooms;

    public enum TypeRoom {
        Nam, Nữ
    }
}