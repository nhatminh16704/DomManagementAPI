package com.domhub.api.service;


import com.domhub.api.dto.request.AccountRequest;
import com.domhub.api.dto.request.ChangePasswordRequest;
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
    private final JwtUtil jwtUtil = new JwtUtil();


    public Account createAccount(AccountRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Account account = new Account();
        account.setUserName(request.getUserName());
        account.setPassword(encodedPassword);

        Role role = roleRepository.findByRoleName(request.getRole());
        account.setRole(role);

        return accountRepository.save(account);
    }

    public void updateAccount(AccountRequest request, Integer id) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            account.setUserName(request.getUserName());
//            account.setPassword(passwordEncoder.encode(request.getPassword()));
            Role role = roleRepository.findByRoleName(request.getRole());
            account.setRole(role);
            accountRepository.save(account);
        }
    }

    public String changePassword(Integer id, ChangePasswordRequest changePasswordRequest) {
        Optional<Account> accountOptional = accountRepository.findById(id);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), account.getPassword())) {
                String encodedPassword = passwordEncoder.encode(changePasswordRequest.getNewPassword());
                account.setPassword(encodedPassword);
                accountRepository.save(account);
                return "changed password";
            }
            throw new RuntimeException("password does not match");
        }
        throw new RuntimeException("account does not exist");
    }

    public String login(String username, String password) {
        Optional<Account> accountOptional = accountRepository.findByUserName(username);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            if (passwordEncoder.matches(password, account.getPassword())) {
                // Tạo JWT token với thông tin bổ sung
                Map<String, Object> claims = new HashMap<>();
                claims.put("id", account.getId());
                claims.put("role", account.getRole().getRoleName());  // Giả sử bạn có đối tượng `Role` trong `Account`

                // Tạo token JWT
                String jwtToken = jwtUtil.generateToken(username, claims);
                return jwtToken; // Trả về JWT
            }
        }
        throw new RuntimeException("Invalid username or password");
    }

    public void deleteAccount(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found with id " + id));
        accountRepository.deleteById(id);
    }
}

