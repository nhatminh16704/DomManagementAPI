package com.domhub.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StaffRequest {

    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotNull(message = "Birthday is required")
    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "Nam|Nữ", message = "Gender must be either 'Nam' or 'Nữ'")
    private String gender;

    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10 or 11 digits")
    private String phoneNumber;

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotBlank(message = "Position is required")
    @Pattern(regexp = "Quản lý ký túc xá|Bảo vệ", message = "Position must be either 'Quản lý ký túc xá' or 'Bảo vệ'")
    private String position;

}

