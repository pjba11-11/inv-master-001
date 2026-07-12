package com.inv.invmaster001.dto.request;

import lombok.Data;

@Data
public class CreateUserAdminRequest {
    private String name;
    private String email;
    private String password;
    private String role;
}
