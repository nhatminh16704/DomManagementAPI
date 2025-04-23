package com.domhub.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentRequest {

    @NotBlank(message = "Student code cannot be empty")
    private String studentCode;

    @NotBlank(message = "Full name cannot be empty")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;

    @NotNull(message = "Birthday cannot be empty")
    @Past(message = "Birthday must be in the past")
    private LocalDate birthday;

    @NotBlank(message = "Gender cannot be empty")
    @Pattern(regexp = "Nam|Nữ", message = "Gender must be Nam or Nữ")
    private String gender;

    @NotBlank(message = "Class name cannot be empty")
    private String className;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone number must be 10 or 11 digits")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @NotBlank(message = "Hometown cannot be empty")
    private String hometown;
}

