package com.domhub.api.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationDTO {
    private String title;
    private String content;
    private LocalDate create_date;
    private String name_person_create;
}
