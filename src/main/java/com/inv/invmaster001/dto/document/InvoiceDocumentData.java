package com.inv.invmaster001.dto.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDocumentData {

    // =========================
    // COMPANY
    // =========================

    private String companyName;
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String companyGst;
    private String bankName;
    private String accountNumber;
    private String ifsc;
    private String logo;

    // =========================
    // CUSTOMER
    // =========================

    private String customerName;
    private String customerAddress;
    private String customerPhone;
    private String customerEmail;
    private String customerGst;

    // =========================
    // INVOICE
    // =========================

    private String invoiceNumber;
    private LocalDate invoiceDate;
    private String poNumber;

    // =========================
    // ITEMS
    // =========================

    private List<InvoiceDocumentItem> items;

    // =========================
    // TOTALS
    // =========================

    private BigDecimal subtotal;
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal discount;
    private BigDecimal grandTotal;
    private String grandTotalWords;

    private String remarks;
}