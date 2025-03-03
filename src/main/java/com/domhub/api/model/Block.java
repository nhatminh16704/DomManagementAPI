package com.domhub.api.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


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

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Room> rooms;

    public enum TypeRoom {
        Nam, Nữ
    }
}