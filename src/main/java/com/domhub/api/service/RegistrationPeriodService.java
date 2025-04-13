package com.domhub.api.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.domhub.api.dto.request.RegistrationPeriodRequest;
import com.domhub.api.dto.response.RegistrationPeriodDTO;
import com.domhub.api.model.RegistrationPeriod;
import com.domhub.api.model.RegistrationPeriod.RegistrationStatus;
import com.domhub.api.repository.RegistrationPeriodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationPeriodService {
    
    private final RegistrationPeriodRepository registrationPeriodRepository;

    public List<RegistrationPeriodDTO> getAll(){
        
        return registrationPeriodRepository.findAllWithCreatorName().stream()
        .sorted((a, b) -> Long.compare(b.getId(), a.getId())) 
        .collect(Collectors.toList());
    }

    public String create(RegistrationPeriodRequest request){
        RegistrationPeriod registrationPeriod = new RegistrationPeriod();
        registrationPeriod.setCreator(request.getCreator());
        registrationPeriod.setName(request.getName());
        registrationPeriod.setStartDate(request.getStartDate());
        registrationPeriod.setEndDate(request.getEndDate());
        registrationPeriod.setIsActive(RegistrationStatus.NOT_STARTED);

        RegistrationPeriod saved = registrationPeriodRepository.save(registrationPeriod);

        return "Created successfully with ID: " + saved.getId(); 
    }
}
