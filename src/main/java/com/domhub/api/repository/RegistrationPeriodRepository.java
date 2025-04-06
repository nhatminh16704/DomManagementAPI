package com.domhub.api.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import com.domhub.api.model.RegistrationPeriod;

public interface RegistrationPeriodRepository extends JpaRepository<RegistrationPeriod,Integer> {
    boolean existsByIsActiveTrue();
}
