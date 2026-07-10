package com.inv.invmaster001.controller;

import com.inv.invmaster001.dto.request.invoice.CreateInvoiceRequest;
import com.inv.invmaster001.dto.request.invoice.CreatePaymentRequest;
import com.inv.invmaster001.dto.request.invoice.UpdateInvoiceRequest;
import com.inv.invmaster001.dto.response.invoice.InvoiceLineItemResponse;
import com.inv.invmaster001.dto.response.invoice.InvoiceListResponse;
import com.inv.invmaster001.dto.response.invoice.InvoicePdfResponse;
import com.inv.invmaster001.dto.response.invoice.InvoiceResponse;
import com.inv.invmaster001.dto.response.invoice.PaymentResponse;
import com.inv.invmaster001.security.CustomUserDetails;
import com.inv.invmaster001.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<InvoiceResponse> createInvoice(
            @Valid @RequestBody CreateInvoiceRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(invoiceService.createInvoice(request, currentUser.getUser()));
    }

    @GetMapping
    public ResponseEntity<List<InvoiceListResponse>> getAll(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                invoiceService.getAllInvoices(currentUser.getUser().getCompany().getId()));
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceResponse> getById(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                invoiceService.getInvoiceById(invoiceId, currentUser.getUser().getCompany().getId()));
    }

    @PutMapping("/{invoiceId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<InvoiceResponse> updateInvoice(
            @PathVariable Long invoiceId,
            @RequestBody UpdateInvoiceRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                invoiceService.updateInvoice(invoiceId, request, currentUser.getUser().getCompany().getId()));
    }

    @GetMapping("/{invoiceId}/line-items")
    public ResponseEntity<List<InvoiceLineItemResponse>> getLineItems(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                invoiceService.getLineItems(invoiceId, currentUser.getUser().getCompany().getId()));
    }

    @GetMapping("/{invoiceId}/payments")
    public ResponseEntity<List<PaymentResponse>> getPayments(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                invoiceService.getPayments(invoiceId, currentUser.getUser().getCompany().getId()));
    }

    @PostMapping("/{invoiceId}/payments")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<PaymentResponse> addPayment(
            @PathVariable Long invoiceId,
            @Valid @RequestBody CreatePaymentRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(invoiceService.addPayment(invoiceId, request, currentUser.getUser().getCompany().getId()));
    }

    @GetMapping("/{invoiceId}/pdf")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<byte[]> downloadPdf(
            @PathVariable Long invoiceId,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        InvoicePdfResponse response = invoiceService.downloadPdf(
                invoiceId, currentUser.getUser().getId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + response.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(response.getPdf());
    }
}
