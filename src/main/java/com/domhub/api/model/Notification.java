package com.domhub.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "notification")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer createdBy;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDate createdDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type; // Loại thông báo

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDate.now();
    }

    public enum NotificationType {
        MAINTENANCE, // Bảo trì
        SECURITY,    // An ninh
        GENERAL,       // Nội quy
        URGENT       // Khẩn cấp
    }
}

