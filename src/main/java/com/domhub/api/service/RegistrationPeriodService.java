package com.domhub.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.domhub.api.dto.response.ApiResponse;
import com.domhub.api.exception.AppException;
import com.domhub.api.exception.ErrorCode;
import com.domhub.api.mapper.RegistrationPeriodMapper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.domhub.api.dto.request.RegistrationPeriodRequest;
import com.domhub.api.dto.response.RegistrationPeriodDTO;
import com.domhub.api.model.RegistrationPeriod;
import com.domhub.api.repository.RegistrationPeriodRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RegistrationPeriodService {

    private final RegistrationPeriodRepository registrationPeriodRepository;
    private final RegistrationPeriodMapper registrationPeriodMapper;

    public ApiResponse<List<RegistrationPeriodDTO>> getAll() {
        return ApiResponse.success(registrationPeriodRepository.findAllWithCreatorName().stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .collect(Collectors.toList()));
    }


    public ApiResponse<Void> create(RegistrationPeriodRequest request) {

        RegistrationPeriod registrationPeriod = registrationPeriodMapper.toEntity(request);
        registrationPeriod.setIsActive(java.time.LocalDateTime.now().isAfter(registrationPeriod.getStartDate()));
        RegistrationPeriod saved = registrationPeriodRepository.save(registrationPeriod);

        return ApiResponse.success("Created successfully with ID: " + saved.getId());
    }

    @Scheduled(cron = "0 0 0 * * *") // Chạy mỗi ngày lúc 00:00
    public void updateRegistrationPeriodStatuses() {
        LocalDateTime now = LocalDateTime.now();
        List<RegistrationPeriod> periods = registrationPeriodRepository.findAll();

        for (RegistrationPeriod period : periods) {
            boolean isNowActive = !now.isBefore(period.getStartDate()) && !now.isAfter(period.getEndDate());

            if (period.getIsActive() != isNowActive) {
                period.setIsActive(isNowActive);
                registrationPeriodRepository.save(period);
            }
        }
    }
}
