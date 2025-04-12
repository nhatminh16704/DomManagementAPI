package com.domhub.api.model;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registrationperiod")
@Getter
@Setter
@NoArgsConstructor
public class RegistrationPeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long creator;
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean isActive;
}
