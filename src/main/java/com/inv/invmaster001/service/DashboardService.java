package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.response.dashboard.DashboardPeriod;
import com.inv.invmaster001.dto.response.dashboard.DashboardPeriodResponse;
import com.inv.invmaster001.dto.response.dashboard.DashboardResponse;
import com.inv.invmaster001.dto.response.dashboard.TopCustomerResponse;
import com.inv.invmaster001.dto.response.dashboard.TopProductResponse;
import com.inv.invmaster001.entity.Customer;
import com.inv.invmaster001.entity.Invoice;
import com.inv.invmaster001.entity.InvoiceSequence;
import com.inv.invmaster001.entity.Payment;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.repository.CustomerRepository;
import com.inv.invmaster001.repository.InvoiceRepository;
import com.inv.invmaster001.repository.InvoiceSequenceRepository;
import com.inv.invmaster001.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DashboardService {

    private final InvoiceSequenceRepository invoiceSequenceRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final PaymentRepository paymentRepository;

    public DashboardResponse getDashboard(User currentUser) {

        Long companyId = currentUser.getCompany().getId();

        List<InvoiceSequence> invoiceSequences =
                invoiceSequenceRepository
                        .findByCompanyIdAndDeletedAtIsNull(companyId);

        List<Invoice> invoices =
                invoiceRepository
                        .findByCompanyIdAndDeletedAtIsNull(companyId);

        List<Customer> customers =
                customerRepository
                        .findByCompanyIdAndDeletedAtIsNull(companyId);

        List<Payment> payments = invoices.stream()
                .flatMap(invoice ->
                        paymentRepository.findByInvoiceId(invoice.getId()).stream())
                .toList();

        List<DashboardPeriodResponse> periods =
                Arrays.stream(DashboardPeriod.values())
                        .map(period ->
                                buildDashboard(
                                        period,
                                        invoiceSequences,
                                        invoices,
                                        customers,
                                        payments))
                        .toList();

        return DashboardResponse.builder()
                .periods(periods)
                .build();
    }

    private DashboardPeriodResponse buildDashboard(
            DashboardPeriod period,
            List<InvoiceSequence> invoiceSequences,
            List<Invoice> invoices,
            List<Customer> customers,
            List<Payment> payments) {

        LocalDate fromDate = getFromDate(period);

        List<InvoiceSequence> filteredSequences =
                invoiceSequences.stream()
                        .filter(sequence ->
                                sequence.getInvoiceDate() != null &&
                                        !sequence.getInvoiceDate().isBefore(fromDate))
                        .toList();

        List<Invoice> filteredInvoices =
                invoices.stream()
                        .filter(invoice ->
                                invoice.getInvoiceDate() != null &&
                                        !invoice.getInvoiceDate().isBefore(fromDate))
                        .toList();

        List<Payment> filteredPayments =
                payments.stream()
                        .filter(payment ->
                                payment.getPaymentDate() != null &&
                                        !payment.getPaymentDate().isBefore(fromDate))
                        .toList();

        BigDecimal revenue =
                calculateRevenue(filteredSequences);

        Integer totalInvoices =
                filteredSequences.size();

        BigDecimal averageInvoiceValue =
                calculateAverageInvoiceValue(
                        revenue,
                        totalInvoices
                );

        Integer totalCustomers =
                customers.size();

        BigDecimal collectedAmount =
                calculateCollectedAmount(filteredPayments);

        BigDecimal outstandingAmount =
                calculateOutstandingAmount(
                        filteredInvoices,
                        collectedAmount
                );

        BigDecimal growthPercentage =
                calculateGrowthPercentage(
                        period,
                        invoiceSequences
                );

        List<TopProductResponse> topProducts =
                calculateTopProducts(filteredSequences);

        List<TopCustomerResponse> topCustomers =
                calculateTopCustomers(filteredInvoices, customers);

        return DashboardPeriodResponse.builder()
                .period(period)
                .revenue(revenue)
                .totalInvoices(totalInvoices)
                .totalCustomers(totalCustomers)
                .averageInvoiceValue(averageInvoiceValue)
                .collectedAmount(collectedAmount)
                .outstandingAmount(outstandingAmount)
                .growthPercentage(growthPercentage)
                .topProducts(topProducts)
                .topCustomers(topCustomers)
                .growthPrediction(null) // AI Phase
                .build();
    }
    // =========================================================
    // REVENUE
    // =========================================================

    private BigDecimal calculateRevenue(
            List<InvoiceSequence> invoiceSequences) {

        return invoiceSequences.stream()
                .map(sequence -> sequence.getResponseJson().getGrandTotal())
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // =========================================================
    // AVERAGE INVOICE VALUE
    // =========================================================

    private BigDecimal calculateAverageInvoiceValue(
            BigDecimal revenue,
            Integer totalInvoices) {

        if (totalInvoices == null || totalInvoices == 0) {
            return BigDecimal.ZERO;
        }

        return revenue.divide(
                BigDecimal.valueOf(totalInvoices),
                2,
                RoundingMode.HALF_UP
        );
    }

    // =========================================================
    // COLLECTED AMOUNT
    // =========================================================

    private BigDecimal calculateCollectedAmount(
            List<Payment> payments) {

        return payments.stream()
                .map(Payment::getAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // =========================================================
    // OUTSTANDING AMOUNT
    // =========================================================

    private BigDecimal calculateOutstandingAmount(
            List<Invoice> invoices,
            BigDecimal collectedAmount) {

        BigDecimal totalInvoiceAmount =
                invoices.stream()
                        .map(Invoice::getGrandTotal)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal outstanding =
                totalInvoiceAmount.subtract(collectedAmount);

        if (outstanding.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return outstanding;
    }

    // =========================================================
    // GROWTH %
    // =========================================================

    private BigDecimal calculateGrowthPercentage(
            DashboardPeriod period,
            List<InvoiceSequence> invoiceSequences) {

        LocalDate currentFrom = getFromDate(period);

        LocalDate previousFrom;
        LocalDate previousTo = currentFrom.minusDays(1);

        switch (period) {

            case LAST_MONTH ->
                    previousFrom = currentFrom.minusMonths(1);

            case LAST_3_MONTHS ->
                    previousFrom = currentFrom.minusMonths(3);

            case LAST_6_MONTHS ->
                    previousFrom = currentFrom.minusMonths(6);

            case LAST_YEAR ->
                    previousFrom = currentFrom.minusYears(1);

            default ->
                    previousFrom = currentFrom.minusMonths(1);
        }

        BigDecimal currentRevenue =
                invoiceSequences.stream()
                        .filter(sequence ->
                                sequence.getInvoiceDate() != null &&
                                        !sequence.getInvoiceDate().isBefore(currentFrom))
                        .map(sequence -> sequence.getResponseJson().getGrandTotal())
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal previousRevenue =
                invoiceSequences.stream()
                        .filter(sequence ->
                                sequence.getInvoiceDate() != null &&
                                        !sequence.getInvoiceDate().isBefore(previousFrom) &&
                                        !sequence.getInvoiceDate().isAfter(previousTo))
                        .map(sequence -> sequence.getResponseJson().getGrandTotal())
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (previousRevenue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return currentRevenue
                .subtract(previousRevenue)
                .multiply(BigDecimal.valueOf(100))
                .divide(previousRevenue, 2, RoundingMode.HALF_UP);
    }

    // =========================================================
    // PERIOD START DATE
    // =========================================================

    private LocalDate getFromDate(
            DashboardPeriod period) {

        LocalDate today = LocalDate.now();

        return switch (period) {

            case LAST_MONTH -> today.minusMonths(1);

            case LAST_3_MONTHS -> today.minusMonths(3);

            case LAST_6_MONTHS -> today.minusMonths(6);

            case LAST_YEAR -> today.minusYears(1);
        };
    }
    // =========================================================
    // TOP PRODUCTS
    // =========================================================

    private List<TopProductResponse> calculateTopProducts(
            List<InvoiceSequence> invoiceSequences) {

        Map<Long, TopProductAccumulator> productMap = new HashMap<>();

        for (InvoiceSequence sequence : invoiceSequences) {

            if (sequence.getResponseJson() == null ||
                    sequence.getResponseJson().getItems() == null) {
                continue;
            }

            sequence.getResponseJson().getItems().forEach(item -> {

                TopProductAccumulator accumulator =
                        productMap.computeIfAbsent(
                                item.getProductId(),
                                id -> new TopProductAccumulator(
                                        item.getProductName()
                                )
                        );

                accumulator.setQuantity(
                        accumulator.getQuantity().add(item.getQuantity())
                );

                accumulator.setRevenue(
                        accumulator.getRevenue().add(item.getTotalPrice())
                );

            });

        }

        return productMap.entrySet()
                .stream()
                .sorted((a, b) ->
                        b.getValue()
                                .getRevenue()
                                .compareTo(a.getValue().getRevenue()))
                .limit(5)
                .map(entry ->
                        TopProductResponse.builder()
                                .productId(entry.getKey())
                                .productName(entry.getValue().getProductName())
                                .quantitySold(entry.getValue().getQuantity())
                                .revenue(entry.getValue().getRevenue())
                                .build())
                .toList();
    }

    // =========================================================
    // TOP CUSTOMERS
    // =========================================================

    private List<TopCustomerResponse> calculateTopCustomers(
            List<Invoice> invoices,
            List<Customer> customers) {

        Map<Long, TopCustomerAccumulator> customerMap = new HashMap<>();

        Map<Long, String> customerNames =
                customers.stream()
                        .collect(Collectors.toMap(
                                Customer::getId,
                                Customer::getCustomerName
                        ));

        for (Invoice invoice : invoices) {

            TopCustomerAccumulator accumulator =
                    customerMap.computeIfAbsent(
                            invoice.getCustomerId(),
                            id -> new TopCustomerAccumulator(
                                    customerNames.getOrDefault(
                                            id,
                                            "Unknown Customer"
                                    )
                            )
                    );

            accumulator.setInvoiceCount(
                    accumulator.getInvoiceCount() + 1
            );

            accumulator.setRevenue(
                    accumulator.getRevenue()
                            .add(invoice.getGrandTotal())
            );

        }

        return customerMap.entrySet()
                .stream()
                .sorted((a, b) ->
                        b.getValue()
                                .getRevenue()
                                .compareTo(a.getValue().getRevenue()))
                .limit(5)
                .map(entry ->
                        TopCustomerResponse.builder()
                                .customerId(entry.getKey())
                                .customerName(entry.getValue().getCustomerName())
                                .invoiceCount(entry.getValue().getInvoiceCount())
                                .revenue(entry.getValue().getRevenue())
                                .build())
                .toList();
    }
    // =========================================================
    // HELPER CLASSES
    // =========================================================

    private static class TopProductAccumulator {

        private final String productName;

        private BigDecimal quantity = BigDecimal.ZERO;

        private BigDecimal revenue = BigDecimal.ZERO;

        public TopProductAccumulator(String productName) {
            this.productName = productName;
        }

        public String getProductName() {
            return productName;
        }

        public BigDecimal getQuantity() {
            return quantity;
        }

        public void setQuantity(BigDecimal quantity) {
            this.quantity = quantity;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

    private static class TopCustomerAccumulator {

        private final String customerName;

        private Integer invoiceCount = 0;

        private BigDecimal revenue = BigDecimal.ZERO;

        public TopCustomerAccumulator(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerName() {
            return customerName;
        }

        public Integer getInvoiceCount() {
            return invoiceCount;
        }

        public void setInvoiceCount(Integer invoiceCount) {
            this.invoiceCount = invoiceCount;
        }

        public BigDecimal getRevenue() {
            return revenue;
        }

        public void setRevenue(BigDecimal revenue) {
            this.revenue = revenue;
        }
    }

}