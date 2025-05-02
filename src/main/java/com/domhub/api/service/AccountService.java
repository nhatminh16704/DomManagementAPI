package com.domhub.api.service;


import com.domhub.api.dto.request.AccountRequest;
import com.domhub.api.dto.request.ChangePasswordRequest;
import com.domhub.api.dto.request.LoginRequest;
import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.domhub.api.model.Account;
import com.domhub.api.model.Role;
import com.domhub.api.repository.AccountRepository;
import com.domhub.api.repository.RoleRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.domhub.api.security.JwtUtil;


@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final HttpServletRequest request;

    public boolean existsById(Integer id) {
        return accountRepository.existsById(id);
    }

    public void validateAccountExists(Integer accountId, String messageIfNotFound) {
        if (!accountRepository.existsById(accountId)) {
            throw new AppException(ErrorCode.USER_NOT_FOUND, messageIfNotFound + " with id " + accountId);
        }
    }

    public void validateAccountExists(Integer accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND, "Account not found with id " + accountId);
        }
    }

    public Account findById(Integer id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND, "Account not found with id " + id));
    }


    public Account createAccount(AccountRequest request) {

        if (accountRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.USERNAME_ALREADY_EXISTS);
        }
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        Account account = new Account();
        account.setUserName(request.getUserName());
        account.setPassword(encodedPassword);
        Role role = roleRepository.findByRoleName(request.getRole()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        account.setRole(role);
        return accountRepository.save(account);

    }

    public void updateAccount(AccountRequest request, Integer id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setUserName(request.getUserName());
//            account.setPassword(passwordEncoder.encode(request.getPassword()));
            Role role = roleRepository.findByRoleName(request.getRole()).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            account.setRole(role);
            accountRepository.save(account);
        }
    }

    public void updateUserName(Integer id, String userName) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setUserName(userName);
            accountRepository.save(account);
        } else {
            throw new AppException(ErrorCode.ACCOUNT_NOT_FOUND);
        }
    }

    public ApiResponse<Void> changePassword(ChangePasswordRequest changePasswordRequest) {
        Integer id = jwtUtil.extractAccountIdFromHeader(request.getHeader("Authorization"));
        Account account = accountRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        if (passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), account.getPassword())) {
            String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
            account.setPassword(encodedPassword);
            accountRepository.save(account);
            return ApiResponse.success("Password changed successfully");
        }
        throw new AppException(ErrorCode.PASSWORD_NOT_MATCH);
    }

    public ApiResponse<String> login(LoginRequest request) {
        Account account = accountRepository.findByUserName(request.getUserName()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            // Tạo JWT token với thông tin bổ sung
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", account.getId());
            claims.put("role", account.getRole().getRoleName());

            // Tạo token JWT
            String token = jwtUtil.generateToken(request.getUserName(), claims);
            return ApiResponse.success(token, "Login successfully");
        }
        throw new AppException(ErrorCode.WRONG_PASSWORD);


    }

    public void deleteAccount(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        accountRepository.deleteById(id);
    }
}

