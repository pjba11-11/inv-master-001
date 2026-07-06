package com.inv.invmaster001.dto.entitydto;

import lombok.Data;

@Data
public class InvoiceSequenceDTO {
    private Long id;
    private Long companyId;
    private Integer year;
    private String prefix;
    private Integer nextNumber;
}