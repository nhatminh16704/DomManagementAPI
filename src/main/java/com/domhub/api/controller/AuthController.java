package com.domhub.api.controller;

import com.domhub.api.dto.request.AccountRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.domhub.api.model.Account;
import com.domhub.api.service.AccountService;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // Cho phép frontend gọi API từ bất kỳ domain nào
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        try {
            String username = request.get("username");
            String password = request.get("password");

            String jwtToken = accountService.login(username, password);

            return ResponseEntity.ok(jwtToken);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }


}

