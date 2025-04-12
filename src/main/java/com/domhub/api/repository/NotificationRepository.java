package com.domhub.api.repository;

import com.domhub.api.dto.response.NotificationDTO;
import com.domhub.api.model.Notification;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}
