package com.domhub.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "type_room")
@Getter
@Setter
public class TypeRoom {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

}
