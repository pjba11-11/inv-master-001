package com.inv.invmaster001.service;


import com.inv.invmaster001.dto.request.customer.CreateCustomerRequest;
import com.inv.invmaster001.dto.request.customer.UpdateCustomerRequest;
import com.inv.invmaster001.dto.response.customer.CustomerFullResponse;
import com.inv.invmaster001.dto.response.customer.CustomerResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Customer;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {


    private final CustomerRepository customerRepository;



    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    public CustomerResponse createCustomer(
            CreateCustomerRequest request,
            User currentUser) {

        Company company = currentUser.getCompany();

        Customer customer = Customer.builder()

                .company(company)

                .createdBy(currentUser)

                .customerName(request.getCustomerName())

                .gstNumber(request.getGstNumber())

                .phone(request.getPhone())

                .email(request.getEmail())

                .address(request.getAddress())

                .bankName(request.getBankName())

                .accountNumber(request.getAccountNumber())

                .ifsc(request.getIfsc())

                .upiId(request.getUpiId())

                .build();



        Customer saved =
                customerRepository.save(customer);



        return CustomerResponse.builder()

                .id(saved.getId())

                .message("Customer created successfully")

                .build();

    }





    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================

    public List<CustomerFullResponse> getAllCustomers(Long companyId) {
        return customerRepository
                .findByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(this::mapToFullResponse)
                .toList();
    }

    // =========================================================
    // GET CUSTOMER BY ID
    // =========================================================

    @Transactional(readOnly = true)
    public CustomerFullResponse getCustomerById(Long customerId, Long companyId) {
        Customer customer = customerRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(customerId, companyId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        return mapToFullResponse(customer);
    }

    // =========================================================
    // DELETE CUSTOMER
    // =========================================================

    public void deleteCustomer(Long customerId, Long companyId) {
        Customer customer = customerRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(customerId, companyId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setDeletedAt(java.time.LocalDateTime.now());
        customerRepository.save(customer);
    }

    private CustomerFullResponse mapToFullResponse(Customer customer) {
        return CustomerFullResponse.builder()
                .id(customer.getId())
                .customerName(customer.getCustomerName())
                .gstNumber(customer.getGstNumber())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .address(customer.getAddress())
                .bankName(customer.getBankName())
                .accountNumber(customer.getAccountNumber())
                .ifsc(customer.getIfsc())
                .upiId(customer.getUpiId())
                .createdByName(customer.getCreatedBy() != null ? customer.getCreatedBy().getName() : null)
                .build();
    }





    // =========================================================
    // UPDATE CUSTOMER
    // =========================================================

    public CustomerResponse updateCustomer(
            Long customerId,
            UpdateCustomerRequest request,
            Long companyId) {



        Customer customer =
                customerRepository.findById(customerId)

                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Customer not found"
                                ));



        if (!customer.getCompany()
                .getId()
                .equals(companyId)) {


            throw new RuntimeException(
                    "You cannot access this customer"
            );

        }



        customer.setCustomerName(
                request.getCustomerName()
        );


        customer.setGstNumber(
                request.getGstNumber()
        );


        customer.setPhone(
                request.getPhone()
        );


        customer.setEmail(
                request.getEmail()
        );


        customer.setAddress(
                request.getAddress()
        );


        customer.setBankName(
                request.getBankName()
        );


        customer.setAccountNumber(
                request.getAccountNumber()
        );


        customer.setIfsc(
                request.getIfsc()
        );


        customer.setUpiId(
                request.getUpiId()
        );



        customerRepository.save(customer);



        return CustomerResponse.builder()

                .id(customer.getId())

                .message("Customer updated successfully")

                .build();

    }

}
