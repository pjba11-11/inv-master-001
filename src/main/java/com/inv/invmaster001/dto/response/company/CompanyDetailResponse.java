package com.inv.invmaster001.dto.response.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDetailResponse {

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
}
