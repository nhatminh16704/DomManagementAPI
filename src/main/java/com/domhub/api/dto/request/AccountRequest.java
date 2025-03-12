package com.domhub.api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountRequest {
    private String userName;
    private String password;
    private String role;
}
