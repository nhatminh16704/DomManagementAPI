package com.domhub.api.service;


import com.domhub.api.dto.request.AccountRequest;
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
import com.domhub.api.util.JwtUtil;



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
}

