package com.domhub.api.model;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "roomrental")
@Getter
@Setter
public class RoomRental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Temporal(TemporalType.DATE)
    private LocalDate startDate;

    @Temporal(TemporalType.DATE)
    private LocalDate endDate;

    private Double price;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE, EXPIRED, PENDING, UNPAID
    }
}

