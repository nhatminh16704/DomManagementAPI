package com.domhub.api.model;


import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "roomrental")
@Getter
@Setter
public class RoomRental {

    @Id
    private String id;

    @Column(nullable = false)
    private Integer studentId;

    @Column(nullable = false)
    private String roomId;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    private double price;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE, EXPIRED, PENDING
    }
}

