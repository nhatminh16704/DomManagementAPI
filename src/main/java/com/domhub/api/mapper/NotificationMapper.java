package com.domhub.api.mapper;


import com.domhub.api.dto.request.NotificationRequest;
import com.domhub.api.dto.response.NotificationDTO;
import com.domhub.api.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "createdBy.userName", target = "createdBy")
    NotificationDTO toDTO(Notification notification);

    List<NotificationDTO> toDTOs(List<Notification> notifications);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", expression = "java(LocalDate.now())")
    @Mapping(target = "type", expression = "java(Notification.NotificationType.valueOf(notificationRequest.getType()))")
    Notification toEntity(NotificationRequest notificationRequest);


}
