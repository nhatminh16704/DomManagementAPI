package com.domhub.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ReportDTO {
    private Integer id;
    private AccountDTO createdBy;
    private String title;
    private String content;
    private LocalDateTime sentDate;
    private String status;

}

