package com.inv.invmaster001.dto.entitydto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String password; // raw password for create/update; not exposed in GET if needed
    private String role;
    private Long companyId; // foreign key to company
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}