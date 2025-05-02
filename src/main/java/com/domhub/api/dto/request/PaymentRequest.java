package com.domhub.api.dto.request;

import com.domhub.api.model.Message;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {

    @NotNull(message = "Amount cannot be null")
    @Min(value = 1, message = "Amount must be greater than 0")
    private int amount;

    @NotBlank(message = "Bank code cannot be blank")
    private String bankCode;

    @NotNull(message = "Reference ID cannot be null")
    private Integer idRef;

}