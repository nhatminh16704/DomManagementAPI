package com.domhub.api.service;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.mapper.NotificationMapper;
import com.domhub.api.model.Account;
import com.domhub.api.model.Notification;
import com.domhub.api.model.Staff;
import com.domhub.api.dto.request.NotificationRequest;
import com.domhub.api.dto.response.NotificationDTO;
import com.domhub.api.repository.NotificationRepository;
import com.domhub.api.repository.StaffRepository;

import com.domhub.api.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final HttpServletRequest httpServletRequest;
    private final JwtUtil jwtUtil;
    private final AccountService accountService;


    public ApiResponse<List<NotificationDTO>> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        List<NotificationDTO> notificationDTOs = notificationMapper.toDTOs(notifications);
        return ApiResponse.success(notificationDTOs);
    }

    public ApiResponse<Void> createNotification(NotificationRequest request) {
        String token = httpServletRequest.getHeader("Authorization");
        Integer accountId = jwtUtil.extractAccountIdFromHeader(token);
        Account createdBy = accountService.findById(accountId);

        Notification notification = notificationMapper.toEntity(request);
        notification.setCreatedBy(createdBy);
        notificationRepository.save(notification);

        return ApiResponse.success("Notification created successfully");

    }

}

