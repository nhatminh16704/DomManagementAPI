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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final StaffRepository staffRepository;

    public long count() {
        return notificationRepository.count();
    }

    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> notificationDTOs= new ArrayList<>();
        for(Notification notification : notifications){
            Staff staff = staffRepository.findByAccountId(notification.getCreatedBy()).orElseThrow(() -> new RuntimeException("Staff not found with id: " + notification.getCreatedBy()));;
            notificationDTOs.add(new NotificationDTO(notification.getId(), notification.getTitle(), notification.getContent(), notification.getType().toString(), notification.getCreatedDate(), staff.getFirstName()+" "+ staff.getLastName()));
        }
        return notificationDTOs;
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

}

