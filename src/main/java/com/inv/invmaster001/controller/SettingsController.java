package com.inv.invmaster001.controller;

import com.inv.invmaster001.dto.request.settings.CreateSettingsRequest;
import com.inv.invmaster001.dto.request.settings.UpdateSettingsRequest;
import com.inv.invmaster001.dto.response.settings.SettingsResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.security.CustomUserDetails;
import com.inv.invmaster001.service.SettingsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/company/settings")
@RequiredArgsConstructor
public class SettingsController {


    private final SettingsService settingsService;



    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','EMPLOYEE')")
    public ResponseEntity<SettingsResponse> getSettings(
            @AuthenticationPrincipal CustomUserDetails currentUser) {


        Company company =
                currentUser.getUser()
                        .getCompany();


        return ResponseEntity.ok(
                settingsService.getSettings(company)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SettingsResponse> create(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody CreateSettingsRequest request) {


        Company company =
                currentUser.getUser()
                        .getCompany();


        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        settingsService.createSettings(
                                company,
                                request
                        )
                );
    }





    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SettingsResponse> update(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestBody UpdateSettingsRequest request) {


        Company company =
                currentUser.getUser()
                        .getCompany();


        return ResponseEntity.ok(
                settingsService.updateSettings(
                        company,
                        request
                )
        );
    }





    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(
            @AuthenticationPrincipal CustomUserDetails currentUser) {


        Company company =
                currentUser.getUser()
                        .getCompany();


        settingsService.deleteSettings(company);


        return ResponseEntity.noContent().build();
    }

}