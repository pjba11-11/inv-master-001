package com.inv.invmaster001.service;


import com.inv.invmaster001.dto.request.customer.CreateCustomerRequest;
import com.inv.invmaster001.dto.request.customer.UpdateCustomerRequest;
import com.inv.invmaster001.dto.response.customer.CustomerFullResponse;
import com.inv.invmaster001.dto.response.customer.CustomerResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Customer;
import com.inv.invmaster001.exception.CompanyNotFoundException;
import com.inv.invmaster001.repository.CompanyRepository;
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

    private final CompanyRepository companyRepository;



    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    public CustomerResponse createCustomer(
            CreateCustomerRequest request,
            Long companyId) {


        Company company =
                companyRepository.findById(companyId)
                        .orElseThrow(() ->
                                new CompanyNotFoundException(
                                        "Company not found"
                                ));



        Customer customer = Customer.builder()

                .company(company)

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

    public List<CustomerFullResponse> getAllCustomers(
            Long companyId) {


        return customerRepository
                .findByCompanyIdAndDeletedAtIsNull(companyId)

                .stream()

                .map(customer ->
                        CustomerFullResponse.builder()

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

                                .build()

                )

                .toList();

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
