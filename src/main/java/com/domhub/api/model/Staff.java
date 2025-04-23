package com.domhub.api.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "staff")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer accountId;

    @Column(nullable = false)
    private String fullName;


    @Column(nullable = false)
    private LocalDate birthday;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private String address;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private String position;

    public enum Gender {
        Nam, Nữ
    }
}