package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.product.CreateProductRequest;
import com.inv.invmaster001.dto.request.product.UpdateProductRequest;
import com.inv.invmaster001.dto.response.product.MaterialResponse;
import com.inv.invmaster001.dto.response.product.ProductFullResponse;
import com.inv.invmaster001.dto.response.product.ProductResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Material;
import com.inv.invmaster001.entity.Product;
import com.inv.invmaster001.entity.ProductPriceHistory;
import com.inv.invmaster001.entity.User;
import com.inv.invmaster001.exception.ProductNotFoundException;
import com.inv.invmaster001.exception.ResourceConflictException;
import com.inv.invmaster001.exception.ResourceNotFoundException;
import com.inv.invmaster001.repository.MaterialRepository;
import com.inv.invmaster001.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final MaterialRepository materialRepository;

    public List<ProductFullResponse> getAllProducts(Long companyId) {
        return productRepository
                .findByCompanyIdAndDeletedAtIsNull(companyId)
                .stream()
                .map(this::mapToFullResponse)
                .toList();
    }

    public ProductFullResponse getProduct(Long productId, Long companyId) {
        Product product = productRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(productId, companyId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));
        return mapToFullResponse(product);
    }

    public ProductResponse createProduct(CreateProductRequest request, User currentUser) {
        Company company = currentUser.getCompany();
        Long companyId = company.getId();

        if (productRepository.existsByCompanyIdAndProductNameIgnoreCaseAndDeletedAtIsNull(companyId, request.getProductName())) {
            throw new ResourceConflictException("Product already exists");
        }

        List<Material> materials = materialRepository.findAllById(request.getMaterialIds());
        if (materials.size() != request.getMaterialIds().size()) {
            throw new ResourceNotFoundException("One or more materials not found");
        }

        Product product = new Product();
        product.setCompany(company);
        product.setCreatedBy(currentUser);
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setHsnCode(request.getHsnCode());
        product.setActive(true);
        product.getMaterials().addAll(materials);

        BigDecimal manufacturingCost = calculateManufacturingCost(materials, request.getLabourCharges());
        BigDecimal sellingPrice = calculateSellingPrice(manufacturingCost, request.getProfitMargin());

        addNewProductPriceVersion(product, manufacturingCost, request.getLabourCharges(), sellingPrice, request.getProfitMargin());

        Product saved = productRepository.save(product);
        return ProductResponse.builder().id(saved.getId()).message("Product created successfully").build();
    }

    public ProductResponse updateProduct(Long productId, UpdateProductRequest request, Long companyId) {
        Product product = productRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(productId, companyId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        if (productRepository.existsByCompanyIdAndProductNameIgnoreCaseAndDeletedAtIsNullAndIdNot(companyId, request.getProductName(), productId)) {
            throw new ResourceConflictException("Product already exists");
        }

        List<Material> materials = materialRepository.findAllById(request.getMaterialIds());
        if (materials.size() != request.getMaterialIds().size()) {
            throw new ResourceNotFoundException("One or more materials not found");
        }

        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setHsnCode(request.getHsnCode());
        product.getMaterials().clear();
        product.getMaterials().addAll(materials);

        BigDecimal manufacturingCost = calculateManufacturingCost(materials, request.getLabourCharges());
        BigDecimal sellingPrice = calculateSellingPrice(manufacturingCost, request.getProfitMargin());

        ProductPriceHistory latestPrice = product.getPriceHistory().stream()
                .filter(h -> h.getEffectiveTo() == null).findFirst().orElse(null);

        boolean priceChanged = latestPrice == null
                || latestPrice.getManufacturingCost().compareTo(manufacturingCost) != 0
                || latestPrice.getLabourCharges().compareTo(request.getLabourCharges()) != 0
                || latestPrice.getProfitMargin().compareTo(request.getProfitMargin()) != 0
                || latestPrice.getSellingPrice().compareTo(sellingPrice) != 0;

        if (priceChanged) {
            addNewProductPriceVersion(product, manufacturingCost, request.getLabourCharges(), sellingPrice, request.getProfitMargin());
        }

        productRepository.save(product);
        return ProductResponse.builder().id(product.getId()).message("Product updated successfully").build();
    }

    public void deleteProduct(Long productId, Long companyId) {
        Product product = productRepository
                .findByIdAndCompanyIdAndDeletedAtIsNull(productId, companyId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        product.setActive(false);
        product.setDeletedAt(LocalDateTime.now());
        product.getMaterials().clear(); // remove join table entries only
        productRepository.save(product);
    }

    private BigDecimal calculateManufacturingCost(List<Material> materials, BigDecimal labourCharges) {
        BigDecimal materialCost = materials.stream()
                .filter(m -> Boolean.TRUE.equals(m.getActive()))
                .map(Material::getCurrentPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return materialCost.add(labourCharges);
    }

    private BigDecimal calculateSellingPrice(BigDecimal manufacturingCost, BigDecimal profitMargin) {
        if (profitMargin == null || profitMargin.compareTo(BigDecimal.ZERO) == 0) {
            return manufacturingCost;
        }
        return manufacturingCost.add(
                manufacturingCost.multiply(profitMargin).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        );
    }

    private void addNewProductPriceVersion(Product product, BigDecimal manufacturingCost,
                                           BigDecimal labourCharges, BigDecimal sellingPrice,
                                           BigDecimal profitMargin) {
        product.getPriceHistory().stream()
                .filter(h -> h.getEffectiveTo() == null)
                .findFirst()
                .ifPresent(h -> h.setEffectiveTo(LocalDate.now()));

        ProductPriceHistory history = ProductPriceHistory.builder()
                .product(product)
                .manufacturingCost(manufacturingCost)
                .labourCharges(labourCharges)
                .sellingPrice(sellingPrice)
                .profitMargin(profitMargin)
                .effectiveFrom(LocalDate.now())
                .effectiveTo(null)
                .build();

        product.addPriceHistory(history);
    }

    private ProductFullResponse mapToFullResponse(Product product) {
        ProductPriceHistory latestPrice = product.getPriceHistory().stream()
                .filter(h -> h.getEffectiveTo() == null).findFirst().orElse(null);

        List<MaterialResponse> materials = product.getMaterials().stream()
                .filter(m -> Boolean.TRUE.equals(m.getActive()))
                .map(m -> MaterialResponse.builder()
                        .materialId(m.getId())
                        .materialName(m.getMaterialName())
                        .unit(m.getUnit())
                        .currentPrice(m.getCurrentPrice())
                        .build())
                .toList();

        return ProductFullResponse.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .active(product.getActive())
                .hsnCode(product.getHsnCode())
                .manufacturingCost(latestPrice != null ? latestPrice.getManufacturingCost() : BigDecimal.ZERO)
                .labourCharges(latestPrice != null ? latestPrice.getLabourCharges() : BigDecimal.ZERO)
                .sellingPrice(latestPrice != null ? latestPrice.getSellingPrice() : BigDecimal.ZERO)
                .profitMargin(latestPrice != null ? latestPrice.getProfitMargin() : BigDecimal.ZERO)
                .materials(materials)
                .createdByName(product.getCreatedBy() != null ? product.getCreatedBy().getName() : null)
                .build();
    }
}
