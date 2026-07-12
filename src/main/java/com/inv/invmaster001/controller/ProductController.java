package com.inv.invmaster001.controller;

import com.inv.invmaster001.dto.request.product.CreateProductRequest;
import com.inv.invmaster001.dto.request.product.UpdateProductRequest;
import com.inv.invmaster001.dto.response.product.ProductFullResponse;
import com.inv.invmaster001.dto.response.product.ProductResponse;
import com.inv.invmaster001.security.CustomUserDetails;
import com.inv.invmaster001.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {


    private final ProductService productService;


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<List<ProductFullResponse>> getAllProducts(
            @AuthenticationPrincipal CustomUserDetails currentUser) {


        Long companyId =
                currentUser.getUser()
                        .getCompany()
                        .getId();


        return ResponseEntity.ok(
                productService.getAllProducts(companyId)
        );
    }


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody CreateProductRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {


        ProductResponse response =
                productService.createProduct(
                        request,
                        currentUser.getUser()
                );


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {


        Long companyId =
                currentUser.getUser()
                        .getCompany()
                        .getId();


        ProductResponse response =
                productService.updateProduct(
                        id,
                        request,
                        companyId
                );


        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {


        Long companyId =
                currentUser.getUser()
                        .getCompany()
                        .getId();


        productService.deleteProduct(
                id,
                companyId
        );


        return ResponseEntity.noContent().build();
    }
}