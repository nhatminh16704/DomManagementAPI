package com.domhub.api.service;

import com.domhub.api.model.Notification;
import com.domhub.api.model.Staff;
import com.domhub.api.controller.NotificationController;
import com.domhub.api.dto.request.NotificationRequest;
import com.domhub.api.dto.response.NotificationDTO;
import com.domhub.api.repository.NotificationRepository;
import com.domhub.api.repository.StaffRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
     private final StaffRepository staffRepository;


    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public String createNotification(NotificationRequest request) {
        try {
            Notification notification = new Notification();
            notification.setTitle(request.getTitle());
            notification.setContent(request.getContent());

            // Convert type safely
            try {
                notification.setType(Notification.NotificationType.valueOf(request.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                return "Invalid notification type: " + request.getType();
            }

            notification.setCreatedBy(request.getCreatedBy());

            notificationRepository.save(notification);
            return "Notification created successfully";
        } catch (Exception e) {
            return "Notification creation failed: " + e.getMessage();
        }
    }

    public NotificationDTO findNotificationById(int id) {
        return notificationRepository.findById(id).map(notification -> {
            String name = staffRepository.findByAccountId(notification.getCreatedBy())
                    .map(staff -> staff.getFirstName() + " " + staff.getLastName())
                    .orElseThrow(() -> new RuntimeException("Staff not found with id: " + notification.getCreatedBy()));
    
            return new NotificationDTO(notification.getTitle(), notification.getContent(), notification.getCreatedDate(), name);
        }).orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
    }
}

