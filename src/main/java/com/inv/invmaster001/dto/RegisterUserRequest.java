package com.inv.invmaster001.dto;

import com.inv.invmaster001.entity.UserRole;
import lombok.Data;

@Data
public class RegisterUserRequest {

    private Long companyId;

    private String name;

    private String email;

    private String password;

    private UserRole role;
}
