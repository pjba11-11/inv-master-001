package com.inv.invmaster001.dto.entitydto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CompanyDTO {
    private Long id;
    private String companyName;
    private String gstNumber;
    private String phone;
    private String email;
    private String address;
    private String bankName;
    private String accountNumber;
    private String ifsc;
    private String upiId;
    private String logoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}