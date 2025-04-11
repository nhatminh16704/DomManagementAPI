package com.domhub.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "violation")
@Getter
@Setter
public class Violation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer studentId;

    @Column(nullable = false)
    private Integer staffId;

    @Column(nullable = false)
    private String violationType;

    @Column(nullable = false)
    private LocalDate reportDate;
}
