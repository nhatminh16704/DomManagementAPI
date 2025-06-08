package com.domhub.api.repository;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.domhub.api.dto.response.RegistrationPeriodDTO;
import com.domhub.api.model.RegistrationPeriod;
import org.springframework.data.repository.query.Param;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod,Integer> {
    @Query("SELECT new com.domhub.api.dto.response.RegistrationPeriodDTO(" +
       "r.id, s.fullName, r.name, r.startDate, r.endDate) " +
       "FROM RegistrationPeriod r " +
       "JOIN Staff s ON r.creator = s.accountId")
    List<RegistrationPeriodDTO> findAllWithCreatorName();

    @Query("SELECT CASE WHEN COUNT(rp) > 0 THEN true ELSE false END " +
            "FROM RegistrationPeriod rp " +
            "WHERE :currentDate BETWEEN rp.startDate AND rp.endDate")
    boolean existsByCurrentDateBetweenStartDateAndEndDate(@Param("currentDate") LocalDateTime currentDate);
}
