package com.inv.invmaster001.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterCompanyRequest {

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
}
