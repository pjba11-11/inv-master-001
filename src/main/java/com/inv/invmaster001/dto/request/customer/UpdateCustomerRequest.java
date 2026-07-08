package com.inv.invmaster001.dto.request.customer;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UpdateCustomerRequest {


    private String customerName;


    private String gstNumber;


    private String phone;


    private String email;


    private String address;


    private String bankName;


    private String accountNumber;


    private String ifsc;


    private String upiId;

}