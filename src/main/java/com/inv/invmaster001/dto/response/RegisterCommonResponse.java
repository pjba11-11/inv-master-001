package com.inv.invmaster001.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCommonResponse {
    private String message;
    private Long companyId;
}
