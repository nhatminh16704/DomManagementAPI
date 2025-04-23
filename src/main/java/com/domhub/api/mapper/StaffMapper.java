package com.domhub.api.mapper;


import com.domhub.api.dto.request.StaffRequest;
import com.domhub.api.model.Staff;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StaffMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accountId", ignore = true)
    @Mapping(target = "gender", expression = "java(Staff.Gender.valueOf(staffRequest.getGender()))")
    Staff toEntity(StaffRequest staffRequest);

}
