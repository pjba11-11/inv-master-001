package com.inv.invmaster001.service;


import com.inv.invmaster001.dto.request.product.CreateProductRequest;
import com.inv.invmaster001.dto.request.product.MaterialRequest;
import com.inv.invmaster001.dto.request.product.UpdateProductRequest;
import com.inv.invmaster001.dto.response.product.ProductResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Material;
import com.inv.invmaster001.entity.MaterialPriceHistory;
import com.inv.invmaster001.entity.Product;
import com.inv.invmaster001.entity.ProductPriceHistory;
import com.inv.invmaster001.repository.CompanyRepository;
import com.inv.invmaster001.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CompanyRepository companyRepository;

    // =========================================================
    // CREATE PRODUCT
    // =========================================================
    public ProductResponse createProduct(CreateProductRequest request) {

        Company company = companyRepository.findById(request.getCompanyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));

        Product product = new Product();
        product.setCompany(company);
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setActive(true);

        // PRODUCT PRICE VERSION
        addNewProductPriceVersion(
                product,
                request.getManufacturingCost(),
                request.getSellingPrice()
        );

        // MATERIALS
        for (MaterialRequest req : request.getMaterials()) {

            Material material = new Material();
            material.setMaterialName(req.getMaterialName());
            material.setUnit(req.getUnit());
            material.setCurrentPrice(req.getCurrentPrice());
            material.setActive(true);

            // MATERIAL PRICE VERSION (UNIFIED)
            addNewMaterialPriceVersion(material, req.getCurrentPrice());

            product.addMaterial(material);
        }

        Product saved = productRepository.save(product);

        return ProductResponse.builder()
                .id(saved.getId())
                .message("Product created successfully")
                .build();
    }

    // =========================================================
    // UPDATE PRODUCT
    // =========================================================
    public ProductResponse updateProduct(Long productId, UpdateProductRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setActive(request.getActive());

        // PRODUCT PRICE VERSIONING
        if (request.getManufacturingCost() != null &&
                request.getSellingPrice() != null) {

            addNewProductPriceVersion(
                    product,
                    request.getManufacturingCost(),
                    request.getSellingPrice()
            );
        }

        // MATERIAL SYNC
        syncMaterials(product, request.getMaterials());

        productRepository.save(product);

        return ProductResponse.builder()
                .id(product.getId())
                .message("Product updated successfully")
                .build();
    }

    // =========================================================
    // DELETE PRODUCT (SOFT DELETE)
    // =========================================================
    public void deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setActive(false);
        product.setDeletedAt(LocalDateTime.now());

        product.getMaterials().forEach(m -> {
            m.setActive(false);
            m.setDeletedAt(LocalDateTime.now());
        });

        productRepository.save(product);
    }

    // =========================================================
    // PRODUCT PRICE VERSIONING
    // =========================================================
    private void addNewProductPriceVersion(Product product,
                                           BigDecimal manufacturingCost,
                                           BigDecimal sellingPrice) {

        product.getPriceHistory().stream()
                .filter(h -> h.getEffectiveTo() == null)
                .findFirst()
                .ifPresent(h -> h.setEffectiveTo(LocalDate.now()));

        ProductPriceHistory history = new ProductPriceHistory();
        history.setProduct(product);
        history.setManufacturingCost(manufacturingCost);
        history.setSellingPrice(sellingPrice);
        history.setProfitMargin(calculateProfitMargin(manufacturingCost, sellingPrice));
        history.setEffectiveFrom(LocalDate.now());
        history.setEffectiveTo(null);

        product.addPriceHistory(history);
    }

    // =========================================================
    // MATERIAL SYNC
    // =========================================================
    private void syncMaterials(Product product, List<MaterialRequest> requests) {

        if (requests == null) return;

        Map<Long, Material> existing = product.getMaterials()
                .stream()
                .filter(m -> m.getId() != null)
                .collect(Collectors.toMap(Material::getId, m -> m));

        Set<Long> incomingIds = new HashSet<>();

        for (MaterialRequest req : requests) {

            // NEW MATERIAL
            if (req.getId() == null) {

                Material material = new Material();
                material.setMaterialName(req.getMaterialName());
                material.setUnit(req.getUnit());
                material.setCurrentPrice(req.getCurrentPrice());
                material.setActive(true);

                addNewMaterialPriceVersion(material, req.getCurrentPrice());

                product.addMaterial(material);
            }

            // UPDATE MATERIAL
            else {

                incomingIds.add(req.getId());

                Material material = existing.get(req.getId());

                if (material != null) {

                    boolean priceChanged =
                            material.getCurrentPrice() == null ||
                                    material.getCurrentPrice().compareTo(req.getCurrentPrice()) != 0;

                    material.setMaterialName(req.getMaterialName());
                    material.setUnit(req.getUnit());

                    if (priceChanged) {
                        material.setCurrentPrice(req.getCurrentPrice());
                        addNewMaterialPriceVersion(material, req.getCurrentPrice());
                    }
                }
            }
        }

        // REMOVE DELETED MATERIALS (from relation only)
        product.getMaterials().removeIf(m ->
                m.getId() != null && !incomingIds.contains(m.getId())
        );
    }

    // =========================================================
    // MATERIAL PRICE VERSIONING (UNIFIED)
    // =========================================================
    private void addNewMaterialPriceVersion(Material material, BigDecimal price) {

        material.getPriceHistory().stream()
                .filter(h -> h.getEffectiveTo() == null)
                .findFirst()
                .ifPresent(h -> h.setEffectiveTo(LocalDate.now()));

        MaterialPriceHistory history = new MaterialPriceHistory();
        history.setMaterial(material);
        history.setPrice(price);
        history.setEffectiveFrom(LocalDate.now());
        history.setEffectiveTo(null);

        material.addPriceHistory(history);
    }

    // =========================================================
    // PROFIT CALCULATION
    // =========================================================
    private BigDecimal calculateProfitMargin(BigDecimal manufacturingCost,
                                             BigDecimal sellingPrice) {

        if (manufacturingCost == null ||
                manufacturingCost.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return sellingPrice
                .subtract(manufacturingCost)
                .divide(manufacturingCost, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}