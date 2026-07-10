package com.inv.invmaster001.controller;


import com.inv.invmaster001.dto.request.company.UpdateCompanyRequest;
import com.inv.invmaster001.dto.response.company.CompanyDetailResponse;
import com.inv.invmaster001.dto.response.company.CompanyResponse;
import com.inv.invmaster001.security.CustomUserDetails;
import com.inv.invmaster001.service.CompanyService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
public class CompanyController {


    private final CompanyService companyService;



    // =========================================================
    // GET COMPANY
    // =========================================================

    @GetMapping
    public ResponseEntity<CompanyDetailResponse> getCompany(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        Long companyId = currentUser.getUser().getCompany().getId();
        return ResponseEntity.ok(companyService.getCompany(companyId));
    }

    // =========================================================
    // UPDATE COMPANY
    // =========================================================

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompanyResponse> updateCompany(

            @Valid @RequestBody UpdateCompanyRequest request,

            @AuthenticationPrincipal CustomUserDetails currentUser

    ) {


        Long companyId =
                currentUser.getUser()
                        .getCompany()
                        .getId();



        return ResponseEntity.ok(

                companyService.updateCompany(
                        companyId,
                        request
                )

        );

    }




    // =========================================================
    // DELETE COMPANY (SOFT DELETE)
    // =========================================================

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCompany(

            @AuthenticationPrincipal CustomUserDetails currentUser

    ) {


        Long companyId =
                currentUser.getUser()
                        .getCompany()
                        .getId();



        companyService.deleteCompany(companyId);



        return ResponseEntity.noContent().build();

    }


}