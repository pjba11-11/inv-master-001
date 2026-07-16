package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.material.CreateMaterialRequest;
import com.inv.invmaster001.dto.request.material.UpdateMaterialRequest;
import com.inv.invmaster001.dto.response.material.MaterialResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Material;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.exception.ResourceNotFoundException;
import com.inv.invmaster001.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MaterialService {

    private final MaterialRepository materialRepository;

    @Transactional(readOnly = true)
    public List<MaterialResponse> getAll(Long companyId) {
        return materialRepository
                .findByCompanyIdAndDeletedAtIsNullOrderByCreatedAtDesc(companyId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MaterialResponse getById(Long id, Long companyId) {
        Material m = materialRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));
        return toResponse(m);
    }

    public MaterialResponse create(CreateMaterialRequest request, User currentUser) {
        Company company = currentUser.getCompany();
        Material material = Material.builder()
                .company(company)
                .materialName(request.getMaterialName())
                .unit(request.getUnit())
                .hsnCode(request.getHsnCode())
                .currentPrice(request.getCurrentPrice())
                .active(request.getActive() != null ? request.getActive() : true)
                .createdBy(currentUser)
                .build();
        return toResponse(materialRepository.save(material));
    }

    public MaterialResponse update(Long id, UpdateMaterialRequest request, Long companyId) {
        Material material = materialRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));

        if (request.getMaterialName() != null) material.setMaterialName(request.getMaterialName());
        if (request.getUnit() != null) material.setUnit(request.getUnit());
        if (request.getHsnCode() != null) material.setHsnCode(request.getHsnCode());
        if (request.getCurrentPrice() != null) material.setCurrentPrice(request.getCurrentPrice());
        if (request.getActive() != null) material.setActive(request.getActive());

        return toResponse(materialRepository.save(material));
    }

    public void delete(Long id, Long companyId) {
        Material material = materialRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(id, companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Material not found"));
        material.setDeletedAt(LocalDateTime.now());
        materialRepository.save(material);
    }

    private MaterialResponse toResponse(Material m) {
        return MaterialResponse.builder()
                .id(m.getId())
                .materialName(m.getMaterialName())
                .unit(m.getUnit())
                .hsnCode(m.getHsnCode())
                .currentPrice(m.getCurrentPrice())
                .active(m.getActive())
                .createdAt(m.getCreatedAt())
                .updatedAt(m.getUpdatedAt())
                .createdByName(m.getCreatedBy() != null ? m.getCreatedBy().getName() : null)
                .build();
    }
}
