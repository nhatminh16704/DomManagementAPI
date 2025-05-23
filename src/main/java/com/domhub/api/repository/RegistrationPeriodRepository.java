package com.domhub.api.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.domhub.api.dto.response.RegistrationPeriodDTO;
import com.domhub.api.model.RegistrationPeriod;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod,Integer> {
    @Query("SELECT new com.domhub.api.dto.response.RegistrationPeriodDTO(" +
       "r.id, CONCAT(CONCAT(s.firstName,' '),s.lastName), r.name, r.startDate, r.endDate, r.isActive) " +
       "FROM RegistrationPeriod r " +
       "JOIN Staff s ON r.creator = s.accountId")
    List<RegistrationPeriodDTO> findAllWithCreatorName();
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM RegistrationPeriod r WHERE r.isActive = 'ACTIVE'")
    boolean existsActiveRegistrationPeriod();
}
