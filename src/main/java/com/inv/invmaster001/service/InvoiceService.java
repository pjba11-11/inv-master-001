package com.inv.invmaster001.service;


import com.inv.invmaster001.dto.request.invoice.CreateInvoiceRequest;
import com.inv.invmaster001.dto.request.invoice.InvoiceItemRequest;
import com.inv.invmaster001.dto.response.invoice.InvoiceLineItemResponse;
import com.inv.invmaster001.dto.response.invoice.InvoiceResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Invoice;
import com.inv.invmaster001.entity.InvoiceLineItem;
import com.inv.invmaster001.entity.InvoiceSequence;
import com.inv.invmaster001.entity.Product;
import com.inv.invmaster001.entity.ProductPriceHistory;
import com.inv.invmaster001.entity.Settings;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.exception.ProductNotFoundException;
import com.inv.invmaster001.repository.CustomerRepository;
import com.inv.invmaster001.repository.InvoiceRepository;
import com.inv.invmaster001.repository.InvoiceSequenceRepository;
import com.inv.invmaster001.repository.ProductPriceHistoryRepository;
import com.inv.invmaster001.repository.ProductRepository;
import com.inv.invmaster001.repository.SettingsRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {


    private final InvoiceRepository invoiceRepository;


    private final InvoiceSequenceRepository invoiceSequenceRepository;


    private final ProductRepository productRepository;


    private final ProductPriceHistoryRepository productPriceHistoryRepository;


    private final SettingsRepository settingsRepository;


    private final CustomerRepository customerRepository;



    // =========================================================
    // CREATE INVOICE
    // =========================================================

    public InvoiceResponse createInvoice(
            CreateInvoiceRequest request,
            User currentUser) {


        Company company =
                currentUser.getCompany();



        // =====================================================
        // CUSTOMER VALIDATION
        // =====================================================

        customerRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(
                        request.getCustomerId(),
                        company.getId()
                )
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found"
                        ));





        Settings settings =
                settingsRepository.findByCompanyId(company.getId())

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Settings not found"
                                ));





        Invoice invoice = Invoice.builder()


                .company(company)


                .createdBy(currentUser)


                .customerId(
                        request.getCustomerId()
                )


                .invoiceDate(
                        LocalDate.now()
                )


                .status(
                        "GENERATED"
                )


                .discount(
                        request.getDiscount() == null
                                ? BigDecimal.ZERO
                                : request.getDiscount()
                )


                .remarks(
                        request.getRemarks()
                )


                .build();





        BigDecimal subtotal =
                BigDecimal.ZERO;



        List<InvoiceLineItemResponse> itemResponses =
                new ArrayList<>();





        // =====================================================
        // CREATE LINE ITEMS
        // =====================================================

        for (InvoiceItemRequest itemRequest :
                request.getItems()) {



            Product product =
                    productRepository
                            .findByIdAndCompanyId(
                                    itemRequest.getProductId(),
                                    company.getId()
                            )

                            .orElseThrow(() ->
                                    new ProductNotFoundException(
                                            "Product not found"
                                    ));





            ProductPriceHistory priceHistory =
                    productPriceHistoryRepository

                            .findByProductIdAndEffectiveToIsNull(
                                    product.getId()
                            )

                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Product price not found"
                                    ));





            BigDecimal unitPrice =
                    priceHistory.getSellingPrice();




            BigDecimal lineTotal =
                    unitPrice.multiply(
                            itemRequest.getQuantity()
                    );




            subtotal =
                    subtotal.add(lineTotal);





            InvoiceLineItem lineItem =
                    InvoiceLineItem.builder()


                            .productId(
                                    product.getId()
                            )


                            .productName(
                                    product.getProductName()
                            )


                            .quantity(
                                    itemRequest.getQuantity()
                            )


                            .unitPrice(
                                    unitPrice
                            )


                            .build();





            invoice.addLineItem(lineItem);





            itemResponses.add(

                    InvoiceLineItemResponse.builder()


                            .productId(
                                    product.getId()
                            )


                            .productName(
                                    product.getProductName()
                            )


                            .quantity(
                                    itemRequest.getQuantity()
                            )


                            .unitPrice(
                                    unitPrice
                            )


                            .totalPrice(
                                    lineTotal
                            )


                            .build()

            );

        }





        // =====================================================
        // TAX CALCULATION
        // =====================================================


        BigDecimal cgstPercentage =
                settings.getCgstPercentage() == null

                        ? BigDecimal.ZERO

                        : settings.getCgstPercentage();




        BigDecimal sgstPercentage =
                settings.getSgstPercentage() == null

                        ? BigDecimal.ZERO

                        : settings.getSgstPercentage();





        BigDecimal cgst =
                calculatePercentage(
                        subtotal,
                        cgstPercentage
                );





        BigDecimal sgst =
                calculatePercentage(
                        subtotal,
                        sgstPercentage
                );





        BigDecimal discount =
                invoice.getDiscount();





        BigDecimal grandTotal =
                subtotal

                        .add(cgst)

                        .add(sgst)

                        .subtract(discount);





        invoice.setSubtotal(subtotal);


        invoice.setCgst(cgst);


        invoice.setSgst(sgst);


        invoice.setGrandTotal(grandTotal);





        // =====================================================
        // SAVE INVOICE
        // =====================================================


        Invoice savedInvoice =
                invoiceRepository.save(invoice);





        // =====================================================
        // GENERATE NUMBER
        // =====================================================


        String invoiceNumber =
                generateInvoiceNumber(
                        company.getId(),
                        settings.getInvoicePrefix(),
                        savedInvoice.getId()
                );





        savedInvoice.setInvoiceNumber(
                invoiceNumber
        );




        invoiceRepository.save(savedInvoice);





        // =====================================================
        // SAVE SEQUENCE
        // =====================================================


        InvoiceSequence sequence =
                InvoiceSequence.builder()


                        .company(company)


                        .invoiceId(
                                savedInvoice.getId()
                        )


                        .invoiceNumber(
                                invoiceNumber
                        )


                        .invoiceDate(
                                LocalDate.now()
                        )


                        .build();





        invoiceSequenceRepository.save(sequence);





        return InvoiceResponse.builder()


                .invoiceId(
                        savedInvoice.getId()
                )


                .invoiceNumber(
                        invoiceNumber
                )


                .invoiceDate(
                        savedInvoice.getInvoiceDate()
                )


                .subtotal(
                        subtotal
                )


                .cgst(
                        cgst
                )


                .sgst(
                        sgst
                )


                .discount(
                        discount
                )


                .grandTotal(
                        grandTotal
                )


                .status(
                        savedInvoice.getStatus()
                )


                .items(
                        itemResponses
                )


                .build();

    }





    // =========================================================
    // TAX HELPER
    // =========================================================

    private BigDecimal calculatePercentage(
            BigDecimal amount,
            BigDecimal percentage) {


        return amount

                .multiply(percentage)

                .divide(
                        BigDecimal.valueOf(100),
                        2,
                        RoundingMode.HALF_UP
                );

    }





    // =========================================================
    // INVOICE NUMBER
    // =========================================================

    private String generateInvoiceNumber(
            Long companyId,
            String prefix,
            Long invoiceId) {


        if (prefix == null || prefix.isBlank()) {

            prefix = "INV";

        }


        return prefix

                + "-"

                + String.format(
                "%05d",
                invoiceId
        );

    }

}