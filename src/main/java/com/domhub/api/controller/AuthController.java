package com.domhub.api.controller;


import com.domhub.api.dto.request.ChangePasswordRequest;
import com.domhub.api.dto.request.LoginRequest;
import com.domhub.api.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.service.AccountService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody @Valid LoginRequest request) {
        return accountService.login(request);
    }

    @PatchMapping("/password-change")
    public ApiResponse<Void> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        return accountService.changePassword(request);
    }

}


