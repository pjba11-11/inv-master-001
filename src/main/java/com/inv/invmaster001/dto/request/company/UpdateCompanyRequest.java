package com.inv.invmaster001.dto.request.company;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateCompanyRequest {


    private String companyName;


    private String gstNumber;


    private String phone;


    private String email;


    private String address;


    private String bankName;


    private String accountNumber;


    private String ifsc;


    private String upiId;


    private String logo;

}