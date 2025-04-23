package com.domhub.api.mapper;

import com.domhub.api.dto.request.RegistrationPeriodRequest;
import com.domhub.api.model.RegistrationPeriod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RegistrationPeriodMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    RegistrationPeriod toEntity(RegistrationPeriodRequest registrationPeriodRequest);
}
