package com.domhub.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.model.Account;
import com.domhub.api.service.AccountService;
import java.util.Optional;

import java.util.Map;


import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API từ bất kỳ domain nào
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Tạo tài khoản mới
    @PostMapping("/register")
    public ResponseEntity<Account> registerAccount(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String roleName = request.get("roleName");

        Account newAccount = accountService.createAccount(username, password, roleName);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        try {
            // Lấy username và password từ body request
            String username = request.get("username");
            String password = request.get("password");


            // Gọi service để xác thực và tạo JWT
            String jwtToken = accountService.login(username, password);

            // Trả về JWT trong response với status 200 (OK)
            return ResponseEntity.ok(jwtToken);
        } catch (RuntimeException e) {
            // Xử lý lỗi từ AccountService (ví dụ: "Invalid username or password")
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }



}

