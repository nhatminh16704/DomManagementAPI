package com.domhub.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateProfileRequest {
    @Email(message = "Email must be in a valid format")
    private String email;

    @Pattern(regexp = "^$|^[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    private String phoneNumber;
}