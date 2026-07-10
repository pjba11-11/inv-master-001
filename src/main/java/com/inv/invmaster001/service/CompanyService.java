package com.inv.invmaster001.service;


import com.inv.invmaster001.dto.request.company.UpdateCompanyRequest;
import com.inv.invmaster001.dto.response.company.CompanyDetailResponse;
import com.inv.invmaster001.dto.response.company.CompanyResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.exception.CompanyNotFoundException;
import com.inv.invmaster001.repository.CompanyRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;



@Service
@RequiredArgsConstructor
@Transactional
public class CompanyService {



    private final CompanyRepository companyRepository;



    // =========================================================
    // GET COMPANY
    // =========================================================

    @Transactional(readOnly = true)
    public CompanyDetailResponse getCompany(Long companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new CompanyNotFoundException("Company not found"));
        return CompanyDetailResponse.builder()
                .id(company.getId())
                .companyName(company.getCompanyName())
                .gstNumber(company.getGstNumber())
                .phone(company.getPhone())
                .email(company.getEmail())
                .address(company.getAddress())
                .bankName(company.getBankName())
                .accountNumber(company.getAccountNumber())
                .ifsc(company.getIfsc())
                .upiId(company.getUpiId())
                .logoUrl(company.getLogo())
                .build();
    }

    // =========================================================
    // UPDATE COMPANY
    // =========================================================

    public CompanyResponse updateCompany(
            Long companyId,
            UpdateCompanyRequest request
    ) {



        Company company =
                companyRepository.findById(companyId)

                        .orElseThrow(() ->
                                new CompanyNotFoundException(
                                        "Company not found"
                                ));




        company.setCompanyName(
                request.getCompanyName()
        );


        company.setGstNumber(
                request.getGstNumber()
        );


        company.setPhone(
                request.getPhone()
        );


        company.setEmail(
                request.getEmail()
        );


        company.setAddress(
                request.getAddress()
        );


        company.setBankName(
                request.getBankName()
        );


        company.setAccountNumber(
                request.getAccountNumber()
        );


        company.setIfsc(
                request.getIfsc()
        );


        company.setUpiId(
                request.getUpiId()
        );


        company.setLogo(
                request.getLogo()
        );



        companyRepository.save(company);



        return CompanyResponse.builder()

                .id(company.getId())

                .message(
                        "Company updated successfully"
                )

                .build();

    }







    // =========================================================
    // DELETE COMPANY (SOFT DELETE)
    // =========================================================

    public void deleteCompany(Long companyId) {



        Company company =
                companyRepository.findById(companyId)

                        .orElseThrow(() ->
                                new CompanyNotFoundException(
                                        "Company not found"
                                ));




        LocalDateTime now =
                LocalDateTime.now();




        company.setDeletedAt(now);



        // =========================
        // SOFT DELETE USERS
        // =========================

        company.getUsers()
                .forEach(user ->
                        user.setDeletedAt(now)
                );




        // =========================
        // SOFT DELETE PRODUCTS
        // =========================

        company.getProducts()
                .forEach(product -> {


                    product.setActive(false);

                    product.setDeletedAt(now);



                    product.getMaterials()
                            .forEach(material -> {

                                material.setActive(false);

                                material.setDeletedAt(now);

                            });


                });




        // =========================
        // SOFT DELETE CUSTOMERS
        // =========================

        company.getCustomers()
                .forEach(customer ->
                        customer.setDeletedAt(now)
                );




        // =========================
        // SOFT DELETE INVOICES
        // =========================

        company.getInvoices()
                .forEach(invoice ->
                        invoice.setDeletedAt(now)
                );




        companyRepository.save(company);

    }


}
