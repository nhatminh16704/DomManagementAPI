package com.domhub.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MessageDetailDTO {
    private Integer id;
    private String title;
    private String content;
    private String sentBy;
    private LocalDateTime date;
}