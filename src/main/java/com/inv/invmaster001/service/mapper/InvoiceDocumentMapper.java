package com.inv.invmaster001.service.mapper;


import com.inv.invmaster001.dto.document.InvoiceDocumentData;
import com.inv.invmaster001.dto.document.InvoiceDocumentItem;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Customer;
import com.inv.invmaster001.entity.Invoice;
import com.inv.invmaster001.entity.InvoiceLineItem;

import org.springframework.stereotype.Component;


import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Component
public class InvoiceDocumentMapper {


    public InvoiceDocumentData map(
            Invoice invoice,
            Company company,
            Customer customer) {


        return InvoiceDocumentData.builder()


                // =========================
                // COMPANY DETAILS
                // =========================

                .companyName(
                        company.getCompanyName()
                )

                .companyAddress(
                        company.getAddress()
                )

                .companyPhone(
                        company.getPhone()
                )

                .companyEmail(
                        company.getEmail()
                )

                .companyGst(
                        company.getGstNumber()
                )

                .bankName(
                        company.getBankName()
                )

                .accountNumber(
                        company.getAccountNumber()
                )

                .ifsc(
                        company.getIfsc()
                )

                .logo(
                        company.getLogo()
                )


                // =========================
                // CUSTOMER DETAILS
                // =========================

                .customerName(
                        customer.getCustomerName()
                )

                .customerAddress(
                        customer.getAddress()
                )

                .customerPhone(
                        customer.getPhone()
                )

                .customerEmail(
                        customer.getEmail()
                )

                .customerGst(
                        customer.getGstNumber()
                )


                // =========================
                // INVOICE DETAILS
                // =========================

                .invoiceNumber(
                        invoice.getInvoiceNumber()
                )

                .invoiceDate(
                        invoice.getInvoiceDate()
                )

                .poNumber(
                        invoice.getPoNumber()
                )


                // =========================
                // ITEMS
                // =========================

                .items(
                        mapItems(
                                invoice.getInvoiceLineItems()
                        )
                )


                // =========================
                // TOTALS
                // =========================

                .subtotal(
                        invoice.getSubtotal()
                )

                .cgst(
                        invoice.getCgst()
                )

                .sgst(
                        invoice.getSgst()
                )

                .discount(
                        invoice.getDiscount()
                )

                .grandTotal(
                        invoice.getGrandTotal()
                )

                .grandTotalWords(
                        invoice.getGrandTotalWords()
                )

                .remarks(
                        invoice.getRemarks()
                )


                .build();

    }



    private List<InvoiceDocumentItem> mapItems(
            List<InvoiceLineItem> items) {


        AtomicInteger counter =
                new AtomicInteger(1);


        return items.stream()

                .map(item ->

                        InvoiceDocumentItem.builder()

                                .serialNumber(
                                        counter.getAndIncrement()
                                )

                                .productName(
                                        item.getProductName()
                                )

                                .hsnCode(
                                        item.getHsnCode()
                                )

                                .quantity(
                                        item.getQuantity()
                                )

                                .unitPrice(
                                        item.getUnitPrice()
                                )

                                .totalPrice(
                                        item.getUnitPrice()
                                                .multiply(
                                                        item.getQuantity()
                                                )
                                )

                                .build()

                )

                .collect(Collectors.toList());

    }

}