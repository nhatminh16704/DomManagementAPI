package com.domhub.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.domhub.api.model.Account;
import java.util.List;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByUserName(String userName);
    List<Account> findByUserNameStartingWith(String username);
    boolean existsByUserName(String userName);

}
