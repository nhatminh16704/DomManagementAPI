package com.domhub.api.mapper;

import com.domhub.api.dto.response.AccountDTO;
import com.domhub.api.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "role.roleName", target = "roleName")
    AccountDTO toDTO(Account account);
}
