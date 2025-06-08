package com.domhub.api.dto.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationDTO {
    private Integer id;
    private String title;
    private String content;
    private String type;
    private LocalDateTime createdDate;
    private String createdBy;
}
