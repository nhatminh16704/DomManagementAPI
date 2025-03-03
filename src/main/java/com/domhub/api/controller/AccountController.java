package com.domhub.api.controller;

import com.domhub.api.model.Account;
import com.domhub.api.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;



@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Tạo tài khoản mới
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String password = request.get("password");
        String roleName = request.get("roleName");

        Account newAccount = accountService.createAccount(username, password, roleName);
        return ResponseEntity.ok(newAccount);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
        boolean isValid = accountService.login(request.get("username"), request.get("password"));
        if (isValid) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

}


