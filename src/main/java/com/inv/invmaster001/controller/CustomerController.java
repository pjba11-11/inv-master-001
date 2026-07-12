package com.inv.invmaster001.controller;


import com.inv.invmaster001.dto.request.customer.CreateCustomerRequest;
import com.inv.invmaster001.dto.request.customer.UpdateCustomerRequest;
import com.inv.invmaster001.dto.response.customer.CustomerFullResponse;
import com.inv.invmaster001.dto.response.customer.CustomerResponse;
import com.inv.invmaster001.security.CustomUserDetails;
import com.inv.invmaster001.service.CustomerService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;


import java.util.List;



@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {



    private final CustomerService customerService;



    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<List<CustomerFullResponse>> getAllCustomers(

            @AuthenticationPrincipal CustomUserDetails currentUser
    ) {


        Long companyId =
                currentUser.getUser()
                        .getCompany()
                        .getId();



        return ResponseEntity.ok(

                customerService.getAllCustomers(companyId)

        );

    }





    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CustomerResponse> createCustomer(

            @Valid @RequestBody CreateCustomerRequest request,

            @AuthenticationPrincipal CustomUserDetails currentUser

    ) {


        return ResponseEntity

                .status(HttpStatus.CREATED)

                .body(
                        customerService.createCustomer(
                                request,
                                currentUser.getUser()
                        )
                );

    }





    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<CustomerFullResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                customerService.getCustomerById(id, currentUser.getUser().getCompany().getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        customerService.deleteCustomer(id, currentUser.getUser().getCompany().getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<CustomerResponse> updateCustomer(

            @PathVariable Long id,

            @Valid @RequestBody UpdateCustomerRequest request,

            @AuthenticationPrincipal CustomUserDetails currentUser

    ) {



        Long companyId =
                currentUser.getUser()
                        .getCompany()
                        .getId();




        return ResponseEntity.ok(

                customerService.updateCustomer(
                        id,
                        request,
                        companyId
                )

        );

    }

}