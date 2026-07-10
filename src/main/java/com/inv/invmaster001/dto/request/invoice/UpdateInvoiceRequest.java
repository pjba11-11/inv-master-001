package com.inv.invmaster001.dto.request.invoice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateInvoiceRequest {

    private String status;
    private String remarks;
}
