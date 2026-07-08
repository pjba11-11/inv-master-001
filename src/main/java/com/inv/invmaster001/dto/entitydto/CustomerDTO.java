package com.inv.invmaster001.dto.entitydto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class CustomerDTO {


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

}