package com.domhub.api.dto.response;

import java.time.LocalDate;

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
    private LocalDate createDate;
    private String createdBy;
}
