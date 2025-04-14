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
    Integer id;

    private String blockName;

    @Column(nullable = false)

    @Enumerated(EnumType.STRING)
    private TypeRoom type;
    
    public enum TypeRoom {
        Nam, Nữ
    }
}