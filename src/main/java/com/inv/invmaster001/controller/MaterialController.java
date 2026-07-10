package com.inv.invmaster001.controller;

import com.inv.invmaster001.dto.request.material.CreateMaterialRequest;
import com.inv.invmaster001.dto.request.material.UpdateMaterialRequest;
import com.inv.invmaster001.dto.response.material.MaterialResponse;
import com.inv.invmaster001.security.CustomUserDetails;
import com.inv.invmaster001.service.MaterialService;
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
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @GetMapping
    public ResponseEntity<List<MaterialResponse>> getAll(
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                materialService.getAll(currentUser.getUser().getCompany().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaterialResponse> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                materialService.getById(id, currentUser.getUser().getCompany().getId()));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<MaterialResponse> create(
            @Valid @RequestBody CreateMaterialRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(materialService.create(request, currentUser.getUser()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<MaterialResponse> update(
            @PathVariable Long id,
            @RequestBody UpdateMaterialRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(
                materialService.update(id, request, currentUser.getUser().getCompany().getId()));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        materialService.delete(id, currentUser.getUser().getCompany().getId());
        return ResponseEntity.noContent().build();
    }
}
