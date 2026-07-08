package com.inv.invmaster001.dto.request.customer;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CreateCustomerRequest {


    @NotBlank
    private String customerName;


    private String gstNumber;


    private String phone;


    @Email
    private String email;


    private String address;


    private String bankName;


    private String accountNumber;


    private String ifsc;


    private String upiId;

}
