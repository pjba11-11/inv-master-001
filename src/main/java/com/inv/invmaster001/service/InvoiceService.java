package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.invoice.CreateInvoiceRequest;
import com.inv.invmaster001.dto.request.invoice.InvoiceItemRequest;
import com.inv.invmaster001.dto.response.invoice.InvoiceLineItemResponse;
import com.inv.invmaster001.dto.response.invoice.InvoicePdfResponse;
import com.inv.invmaster001.dto.response.invoice.InvoiceResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Customer;
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
import com.inv.invmaster001.repository.UserRepository;
import com.inv.invmaster001.service.document.InvoiceDocumentService;
import com.inv.invmaster001.util.NumberToWordsUtil;
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

    private final InvoiceDocumentService invoiceDocumentService;

    private final UserRepository userRepository;

    // =========================================================
    // CREATE INVOICE
    // =========================================================

    public InvoiceResponse createInvoice(
            CreateInvoiceRequest request,
            User currentUser) {

        Company company = currentUser.getCompany();

        // =====================================================
        // VALIDATE ITEMS
        // =====================================================

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new RuntimeException("Invoice must contain at least one item");
        }

        // =====================================================
        // CUSTOMER VALIDATION
        // =====================================================

        Customer customer =
                customerRepository
                        .findByIdAndCompanyIdAndDeletedAtIsNull(
                                request.getCustomerId(),
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Customer not found"));

        // =====================================================
        // SETTINGS
        // =====================================================

        Settings settings =
                settingsRepository
                        .findByCompanyIdAndDeletedAtIsNull(
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Settings not found"));

        // =====================================================
        // CREATE INVOICE ENTITY
        // =====================================================

        Invoice invoice = Invoice.builder()
                .company(company)
                .createdBy(currentUser)
                .customerId(customer.getId())
                .invoiceDate(LocalDate.now())
                .status("GENERATED")
                .discount(
                        request.getDiscount() == null
                                ? BigDecimal.ZERO
                                : request.getDiscount()
                )
                .remarks(request.getRemarks())
                .build();

        BigDecimal subtotal = BigDecimal.ZERO;

        List<InvoiceLineItemResponse> itemResponses =
                new ArrayList<>();

        // =====================================================
        // CREATE LINE ITEMS
        // =====================================================

        for (InvoiceItemRequest itemRequest : request.getItems()) {

            Product product =
                    productRepository
                            .findByIdAndCompanyIdAndDeletedAtIsNull(
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
                            .productId(product.getId())
                            .productName(product.getProductName())
                            .hsnCode(product.getHsnCode())
                            .quantity(itemRequest.getQuantity())
                            .unitPrice(unitPrice)
                            .build();

            invoice.addLineItem(lineItem);

            itemResponses.add(
                    InvoiceLineItemResponse.builder()
                            .productId(product.getId())
                            .productName(product.getProductName())
                            .hsnCode(product.getHsnCode())
                            .quantity(itemRequest.getQuantity())
                            .unitPrice(unitPrice)
                            .totalPrice(lineTotal)
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
        invoice.setPoNumber(request.getPoNumber());
        invoice.setCgstPercentage(cgstPercentage);
        invoice.setSgstPercentage(sgstPercentage);
        invoice.setCgst(cgst);
        invoice.setSgst(sgst);
        invoice.setGrandTotal(grandTotal);
        invoice.setGrandTotalWords(
                NumberToWordsUtil.convert(grandTotal)
        );
        // =====================================================
        // SAVE INVOICE
        // =====================================================

        Invoice savedInvoice =
                invoiceRepository.save(invoice);


        // =====================================================
        // GENERATE INVOICE NUMBER
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


        // =====================================================
        // RESPONSE
        // =====================================================

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
                .cgstPercentage(
                        cgstPercentage
                )

                .sgstPercentage(
                        sgstPercentage
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
                        RoundingMode.HALF_UP);
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
    @Transactional(readOnly = true)
    public InvoicePdfResponse downloadPdf(
            Long invoiceId,
            Long userId) {

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Company company = currentUser.getCompany();


        Invoice invoice =
                invoiceRepository
                        .findById(invoiceId)
                        .orElseThrow(() ->
                                new RuntimeException("Invoice not found"));

        if (!invoice.getCompany().getId().equals(company.getId())) {
            throw new RuntimeException("Invoice does not belong to your company");
        }

        Customer customer =
                customerRepository
                        .findByIdAndCompanyIdAndDeletedAtIsNull(
                                invoice.getCustomerId(),
                                company.getId()
                        )
                        .orElseThrow(() ->
                                new RuntimeException("Customer not found"));

        byte[ ] pdf= invoiceDocumentService.generatePdf(
                invoice,
                company,
                customer
        );
        return new InvoicePdfResponse(
                invoice.getInvoiceNumber() + ".pdf",
                pdf
        );

    }
}