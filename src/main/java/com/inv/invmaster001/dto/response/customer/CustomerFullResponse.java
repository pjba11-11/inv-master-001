package com.inv.invmaster001.dto.response.customer;


import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class CustomerFullResponse {


    private Long id;


    private String customerName;


    private String gstNumber;


    private String phone;


    private String email;


    private String address;


    private String bankName;


    private String accountNumber;


    private String ifsc;


    private String upiId;
    private String createdByName;

}
