package com.domhub.api.repository;

import com.domhub.api.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface StaffRepository extends JpaRepository<Staff, Integer> {
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<Staff> findByAccountId(Integer accountId);
    Optional<Integer> findStaffIdByAccountId(Integer accountId);
}
