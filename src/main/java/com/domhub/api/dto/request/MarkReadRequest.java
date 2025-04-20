package com.domhub.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MarkReadRequest {

    @NotNull(message = "Reader accountId is required")
    @Min(value = 1, message = "Invalid accountId")
    private Integer accountId;
}

