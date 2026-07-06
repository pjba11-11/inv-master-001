package com.inv.invmaster001.dto.entitydto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerDTO {
    private Long id;
    private Long companyId;
    private String customerName;
    private String phone;
    private String email;
    private String gstNumber;
    private String billingAddress;
    private String shippingAddress;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}