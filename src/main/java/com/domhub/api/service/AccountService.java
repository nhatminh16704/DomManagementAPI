package com.domhub.api.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.domhub.api.model.Account;
import com.domhub.api.model.Role;
import com.domhub.api.repository.AccountRepository;
import com.domhub.api.repository.RoleRepository;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public AccountService(AccountRepository accountRepository,
                          PasswordEncoder passwordEncoder,
                          RoleRepository roleRepository) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    public Account createAccount(String username, String password, String roleName) {
        String encodedPassword = passwordEncoder.encode(password);

        Account account = new Account();
        account.setUserName(username);
        account.setPassword(encodedPassword);

        Role role = roleRepository.findByRoleName(roleName);
        account.setRole(role);

        return accountRepository.save(account);
    }

    public boolean login(String username, String password) {
        Optional<Account> accountOptional = accountRepository.findByUserName(username);
        return accountOptional.map(account -> passwordEncoder.matches(password, account.getPassword()))
                .orElse(false);
    }
}

