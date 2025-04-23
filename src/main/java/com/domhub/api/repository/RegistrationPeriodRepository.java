package com.domhub.api.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.domhub.api.dto.response.RegistrationPeriodDTO;
import com.domhub.api.model.RegistrationPeriod;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod,Integer> {
    @Query("SELECT new com.domhub.api.dto.response.RegistrationPeriodDTO(" +
       "r.id, s.fullName, r.name, r.startDate, r.endDate, r.isActive) " +
       "FROM RegistrationPeriod r " +
       "JOIN Staff s ON r.creator = s.accountId")
    List<RegistrationPeriodDTO> findAllWithCreatorName();

    boolean existsByIsActiveTrue();
}
