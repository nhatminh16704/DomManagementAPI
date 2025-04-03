package com.domhub.api.repository;

import com.domhub.api.dto.response.NotificationDTO;
import com.domhub.api.model.Notification;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    @Query("SELECT new com.domhub.api.dto.response.NotificationDTO(" +
            "n.title, n.content, n.createdDate, " +
            "COALESCE(s.firstName, '') || ' ' || COALESCE(s.lastName, '')) " +
            "FROM Notification n " +
            "LEFT JOIN Staff s ON s.accountId = n.createdBy " +
            "WHERE n.id = :id")
    Optional<NotificationDTO> findNotificationDTOById(@Param("id") Integer id);
}
