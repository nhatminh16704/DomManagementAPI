package com.domhub.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileRequest {
    private String email;
    private String phoneNumber;
}
