package com.domhub.api.dto.request;

    import lombok.Data;

    @Data
    public class PaymentRequest {
        private int amount;
        private String bankCode;
        private Integer idRef;
    }