package com.inv.invmaster001.service.document;

import com.inv.invmaster001.dto.document.InvoiceDocumentData;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;

@Service
@RequiredArgsConstructor
public class InvoicePdfService {

    private final TemplateEngine templateEngine;

    public byte[] generateInvoice(
            InvoiceDocumentData data) {

        Context context = new Context();

        context.setVariable(
                "companyName",
                data.getCompanyName()
        );

        context.setVariable(
                "companyAddress",
                data.getCompanyAddress()
        );

        context.setVariable(
                "companyPhone",
                data.getCompanyPhone()
        );

        context.setVariable(
                "companyEmail",
                data.getCompanyEmail()
        );

        context.setVariable(
                "companyGst",
                data.getCompanyGst()
        );

        context.setVariable(
                "bankName",
                data.getBankName()
        );

        context.setVariable(
                "accountNumber",
                data.getAccountNumber()
        );

        context.setVariable(
                "ifsc",
                data.getIfsc()
        );

        context.setVariable(
                "logo",
                data.getLogo()
        );

        context.setVariable(
                "customerName",
                data.getCustomerName()
        );

        context.setVariable(
                "customerAddress",
                data.getCustomerAddress()
        );

        context.setVariable(
                "customerPhone",
                data.getCustomerPhone()
        );

        context.setVariable(
                "customerEmail",
                data.getCustomerEmail()
        );

        context.setVariable(
                "customerGst",
                data.getCustomerGst()
        );

        context.setVariable(
                "invoiceNumber",
                data.getInvoiceNumber()
        );

        context.setVariable(
                "invoiceDate",
                data.getInvoiceDate()
        );

        context.setVariable(
                "poNumber",
                data.getPoNumber()
        );

        context.setVariable(
                "items",
                data.getItems()
        );

        context.setVariable(
                "subtotal",
                data.getSubtotal()
        );

        context.setVariable(
                "cgst",
                data.getCgst()
        );

        context.setVariable(
                "sgst",
                data.getSgst()
        );

        context.setVariable(
                "discount",
                data.getDiscount()
        );

        context.setVariable(
                "grandTotal",
                data.getGrandTotal()
        );

        context.setVariable(
                "grandTotalWords",
                data.getGrandTotalWords()
        );

        context.setVariable(
                "remarks",
                data.getRemarks()
        );

        String html =
                templateEngine.process(
                        "invoice",
                        context
                );

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            PdfRendererBuilder builder =
                    new PdfRendererBuilder();

            builder.useFastMode();

            builder.withHtmlContent(
                    html,
                    null
            );

            builder.toStream(
                    outputStream
            );

            builder.run();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to generate PDF",
                    e
            );

        }

    }

}