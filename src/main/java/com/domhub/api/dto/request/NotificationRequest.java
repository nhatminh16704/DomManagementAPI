package com.domhub.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {
    private String title;
    private String content;
    private String type;
    private Integer createdBy;
}

