package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.invoice.CreateInvoiceRequest;
import com.inv.invmaster001.dto.request.invoice.CreatePaymentRequest;
import com.inv.invmaster001.dto.request.invoice.InvoiceItemRequest;
import com.inv.invmaster001.dto.response.invoice.InvoiceResponse;
import com.inv.invmaster001.dto.response.invoice.PaymentResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Customer;
import com.inv.invmaster001.entity.Invoice;
import com.inv.invmaster001.entity.Payment;
import com.inv.invmaster001.entity.Product;
import com.inv.invmaster001.entity.ProductPriceHistory;
import com.inv.invmaster001.entity.Settings;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.exception.ProductNotFoundException;
import com.inv.invmaster001.repository.CustomerRepository;
import com.inv.invmaster001.repository.InvoiceRepository;
import com.inv.invmaster001.repository.InvoiceSequenceRepository;
import com.inv.invmaster001.repository.PaymentRepository;
import com.inv.invmaster001.repository.ProductPriceHistoryRepository;
import com.inv.invmaster001.repository.ProductRepository;
import com.inv.invmaster001.repository.SettingsRepository;
import com.inv.invmaster001.repository.UserRepository;
import com.inv.invmaster001.service.document.InvoiceDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock private InvoiceRepository invoiceRepository;
    @Mock private InvoiceSequenceRepository invoiceSequenceRepository;
    @Mock private ProductRepository productRepository;
    @Mock private ProductPriceHistoryRepository productPriceHistoryRepository;
    @Mock private SettingsRepository settingsRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private InvoiceDocumentService invoiceDocumentService;
    @Mock private PaymentRepository paymentRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks private InvoiceService invoiceService;

    private User currentUser;
    private Company company;
    private Customer customer;

    @BeforeEach
    void setUp() {
        company = Company.builder().id(1L).build();
        currentUser = User.builder().id(10L).name("Admin").company(company).build();
        customer = Customer.builder().id(5L).customerName("Sharma Traders").company(company).build();
    }

    private void stubInvoiceSaveAssignsId() {
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(inv -> {
            Invoice i = inv.getArgument(0);
            if (i.getId() == null) i.setId(42L);
            return i;
        });
    }

    private void stubProduct(Long productId, String price) {
        Product product = Product.builder()
                .id(productId).productName("Box").hsnCode("4819").company(company).build();
        when(productRepository.findByIdAndCompanyIdAndDeletedAtIsNull(productId, 1L))
                .thenReturn(Optional.of(product));
        when(productPriceHistoryRepository.findByProductIdAndEffectiveToIsNull(productId))
                .thenReturn(Optional.of(ProductPriceHistory.builder()
                        .sellingPrice(new BigDecimal(price)).build()));
    }

    private CreateInvoiceRequest requestWith(List<InvoiceItemRequest> items, String discount) {
        return CreateInvoiceRequest.builder()
                .customerId(5L)
                .items(items)
                .discount(discount == null ? null : new BigDecimal(discount))
                .build();
    }

    // =====================================================
    // createInvoice
    // =====================================================

    @Test
    void createInvoice_calculatesTaxesTotalsAndNumber() {
        when(customerRepository.findByIdAndCompanyIdAndDeletedAtIsNull(5L, 1L))
                .thenReturn(Optional.of(customer));
        when(settingsRepository.findByCompanyIdAndDeletedAtIsNull(1L))
                .thenReturn(Optional.of(Settings.builder()
                        .cgstPercentage(new BigDecimal("9"))
                        .sgstPercentage(new BigDecimal("9"))
                        .invoicePrefix("ACME")
                        .build()));
        stubProduct(7L, "100");
        stubInvoiceSaveAssignsId();

        InvoiceResponse response = invoiceService.createInvoice(
                requestWith(List.of(InvoiceItemRequest.builder()
                        .productId(7L).quantity(new BigDecimal("2")).build()), "10"),
                currentUser);

        assertThat(response.getSubtotal()).isEqualByComparingTo("200");
        assertThat(response.getCgst()).isEqualByComparingTo("18.00");
        assertThat(response.getSgst()).isEqualByComparingTo("18.00");
        assertThat(response.getGrandTotal()).isEqualByComparingTo("226.00");
        assertThat(response.getInvoiceNumber()).isEqualTo("ACME-00042");
        assertThat(response.getStatus()).isEqualTo("GENERATED");
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getTotalPrice()).isEqualByComparingTo("200");

        verify(invoiceSequenceRepository).save(any());
        verify(invoiceRepository, atLeastOnce()).save(any(Invoice.class));
    }

    @Test
    void createInvoice_roundsTaxHalfUp() {
        when(customerRepository.findByIdAndCompanyIdAndDeletedAtIsNull(5L, 1L))
                .thenReturn(Optional.of(customer));
        when(settingsRepository.findByCompanyIdAndDeletedAtIsNull(1L))
                .thenReturn(Optional.of(Settings.builder()
                        .cgstPercentage(new BigDecimal("9"))
                        .sgstPercentage(new BigDecimal("9"))
                        .build()));
        stubProduct(7L, "33.33");
        stubInvoiceSaveAssignsId();

        InvoiceResponse response = invoiceService.createInvoice(
                requestWith(List.of(InvoiceItemRequest.builder()
                        .productId(7L).quantity(BigDecimal.ONE).build()), null),
                currentUser);

        // 33.33 * 9% = 2.9997 -> HALF_UP -> 3.00
        assertThat(response.getCgst()).isEqualByComparingTo("3.00");
    }

    @Test
    void createInvoice_missingSettingsMeansZeroTaxAndDefaultPrefix() {
        when(customerRepository.findByIdAndCompanyIdAndDeletedAtIsNull(5L, 1L))
                .thenReturn(Optional.of(customer));
        when(settingsRepository.findByCompanyIdAndDeletedAtIsNull(1L))
                .thenReturn(Optional.empty());
        stubProduct(7L, "100");
        stubInvoiceSaveAssignsId();

        InvoiceResponse response = invoiceService.createInvoice(
                requestWith(List.of(InvoiceItemRequest.builder()
                        .productId(7L).quantity(BigDecimal.ONE).build()), null),
                currentUser);

        assertThat(response.getCgst()).isEqualByComparingTo("0.00");
        assertThat(response.getSgst()).isEqualByComparingTo("0.00");
        assertThat(response.getInvoiceNumber()).isEqualTo("INV-00042");
        assertThat(response.getGrandTotal()).isEqualByComparingTo("100");
    }

    @Test
    void createInvoice_rejectsEmptyOrNullItems() {
        assertThatThrownBy(() -> invoiceService.createInvoice(requestWith(List.of(), null), currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invoice must contain at least one item");
        assertThatThrownBy(() -> invoiceService.createInvoice(requestWith(null, null), currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invoice must contain at least one item");

        verifyNoInteractions(invoiceRepository, productRepository, invoiceSequenceRepository);
    }

    @Test
    void createInvoice_customerNotFound() {
        when(customerRepository.findByIdAndCompanyIdAndDeletedAtIsNull(5L, 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.createInvoice(
                requestWith(List.of(InvoiceItemRequest.builder()
                        .productId(7L).quantity(BigDecimal.ONE).build()), null),
                currentUser))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Customer not found");
    }

    @Test
    void createInvoice_productNotFound() {
        when(customerRepository.findByIdAndCompanyIdAndDeletedAtIsNull(5L, 1L))
                .thenReturn(Optional.of(customer));
        when(settingsRepository.findByCompanyIdAndDeletedAtIsNull(1L))
                .thenReturn(Optional.empty());
        when(productRepository.findByIdAndCompanyIdAndDeletedAtIsNull(7L, 1L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.createInvoice(
                requestWith(List.of(InvoiceItemRequest.builder()
                        .productId(7L).quantity(BigDecimal.ONE).build()), null),
                currentUser))
                .isInstanceOf(ProductNotFoundException.class);
    }

    // =====================================================
    // addPayment
    // =====================================================

    private Invoice invoiceWithGrandTotal(String grandTotal) {
        return Invoice.builder()
                .id(42L)
                .company(company)
                .grandTotal(new BigDecimal(grandTotal))
                .status("GENERATED")
                .build();
    }

    private void stubPayment(Invoice invoice, String... paymentAmounts) {
        when(invoiceRepository.findByIdAndCompanyIdAndDeletedAtIsNull(42L, 1L))
                .thenReturn(Optional.of(invoice));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment p = inv.getArgument(0);
            p.setId(99L);
            return p;
        });
        List<Payment> payments = java.util.Arrays.stream(paymentAmounts)
                .map(a -> Payment.builder().amount(new BigDecimal(a)).build())
                .toList();
        when(paymentRepository.findByInvoiceId(42L)).thenReturn(payments);
    }

    private CreatePaymentRequest paymentRequest(String amount) {
        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setAmount(new BigDecimal(amount));
        request.setPaymentDate(LocalDate.of(2026, 7, 13));
        request.setPaymentMethod("UPI");
        request.setTransactionReference("UPI-1");
        return request;
    }

    @Test
    void addPayment_fullPaymentMarksInvoicePaid() {
        Invoice invoice = invoiceWithGrandTotal("100");
        stubPayment(invoice, "100");

        PaymentResponse response = invoiceService.addPayment(42L, paymentRequest("100"), 1L);

        ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("PAID");
        assertThat(response.getAmount()).isEqualByComparingTo("100");
        assertThat(response.getPaymentMethod()).isEqualTo("UPI");
        assertThat(response.getTransactionReference()).isEqualTo("UPI-1");
    }

    @Test
    void addPayment_partialPaymentMarksPartiallyPaid() {
        Invoice invoice = invoiceWithGrandTotal("100");
        stubPayment(invoice, "50");

        invoiceService.addPayment(42L, paymentRequest("50"), 1L);

        ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("PARTIALLY_PAID");
    }

    @Test
    void addPayment_overpaymentStillMarksPaid() {
        Invoice invoice = invoiceWithGrandTotal("100");
        stubPayment(invoice, "70", "50");

        invoiceService.addPayment(42L, paymentRequest("50"), 1L);

        ArgumentCaptor<Invoice> captor = ArgumentCaptor.forClass(Invoice.class);
        verify(invoiceRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo("PAID");
    }

    @Test
    void addPayment_invoiceNotFound() {
        when(invoiceRepository.findByIdAndCompanyIdAndDeletedAtIsNull(anyLong(), anyLong()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.addPayment(42L, paymentRequest("50"), 1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Invoice not found");
    }
}
