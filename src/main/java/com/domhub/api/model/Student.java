package com.domhub.api.model;


import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "student")
@Getter
@Setter
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String studentCode;

    private String firstName;
    private String lastName;

    private LocalDate birthday;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String className;

    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String email;

    private String hometown;

    public enum Gender {
        Nam, Nữ
    }
}
