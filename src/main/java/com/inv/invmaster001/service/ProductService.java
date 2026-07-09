package com.inv.invmaster001.service;

import com.inv.invmaster001.dto.request.product.CreateProductRequest;
import com.inv.invmaster001.dto.request.product.MaterialRequest;
import com.inv.invmaster001.dto.request.product.UpdateProductRequest;
import com.inv.invmaster001.dto.response.product.MaterialResponse;
import com.inv.invmaster001.dto.response.product.ProductFullResponse;
import com.inv.invmaster001.dto.response.product.ProductResponse;
import com.inv.invmaster001.entity.Company;
import com.inv.invmaster001.entity.Material;
import com.inv.invmaster001.entity.MaterialPriceHistory;
import com.inv.invmaster001.entity.Product;
import com.inv.invmaster001.entity.ProductPriceHistory;
import com.inv.invmaster001.exception.CompanyNotFoundException;
import com.inv.invmaster001.exception.ProductNotFoundException;
import com.inv.invmaster001.repository.CompanyRepository;
import com.inv.invmaster001.repository.ProductRepository;
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
    // GET ALL PRODUCTS
    // =========================================================

    public List<ProductFullResponse> getAllProducts(Long companyId) {

        return productRepository.findByCompanyId(companyId)
                .stream()
                .map(this::mapToFullResponse)
                .toList();

    }


    // =========================================================
    // CREATE PRODUCT
    // =========================================================

    public ProductResponse createProduct(
            CreateProductRequest request,
            Long companyId) {


        Company company =
                companyRepository.findById(companyId)
                        .orElseThrow(() ->
                                new CompanyNotFoundException(
                                        "Company not found"
                                ));


        boolean productExists =
                productRepository
                        .existsByCompanyIdAndProductNameIgnoreCase(
                                companyId,
                                request.getProductName()
                        );


        if (productExists) {

            throw new RuntimeException(
                    "Product already exists"
            );

        }


        Product product = new Product();

        product.setCompany(company);
        product.setHsnCode(request.getHsnCode());
        product.setProductName(request.getProductName());
        product.setDescription(request.getDescription());
        product.setActive(true);


        addNewProductPriceVersion(
                product,
                request.getManufacturingCost(),
                request.getSellingPrice()
        );


        if (request.getMaterials() != null) {

            for (MaterialRequest req : request.getMaterials()) {


                Material material = new Material();

                material.setMaterialName(req.getMaterialName());
                material.setUnit(req.getUnit());
                material.setCurrentPrice(req.getCurrentPrice());
                material.setActive(true);


                product.addMaterial(material);


                addNewMaterialPriceVersion(
                        material,
                        req.getCurrentPrice()
                );

            }

        }


        Product saved =
                productRepository.save(product);


        return ProductResponse.builder()
                .id(saved.getId())
                .message("Product created successfully")
                .build();

    }

    // =========================================================
    // UPDATE PRODUCT
    // =========================================================
    public ProductResponse updateProduct(
            Long productId,
            UpdateProductRequest request,
            Long companyId) {


        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found"
                                ));


        if (!product.getCompany()
                .getId()
                .equals(companyId)) {

            throw new RuntimeException(
                    "You cannot access this product"
            );

        }


        product.setProductName(
                request.getProductName()
        );


        product.setDescription(
                request.getDescription()
        );


        product.setActive(
                request.getActive()
        );

        product.setHsnCode(
                request.getHsnCode()
        );

        if (request.getManufacturingCost() != null
                &&
                request.getSellingPrice() != null) {


            ProductPriceHistory latestPrice =
                    product.getPriceHistory()
                            .stream()
                            .filter(history ->
                                    history.getEffectiveTo() == null)
                            .findFirst()
                            .orElse(null);


            boolean priceChanged =
                    latestPrice == null
                            ||
                            latestPrice.getManufacturingCost()
                                    .compareTo(request.getManufacturingCost()) != 0
                            ||
                            latestPrice.getSellingPrice()
                                    .compareTo(request.getSellingPrice()) != 0;


            if (priceChanged) {

                addNewProductPriceVersion(
                        product,
                        request.getManufacturingCost(),
                        request.getSellingPrice()
                );

            }

        }


        syncMaterials(
                product,
                request.getMaterials()
        );


        productRepository.save(product);


        return ProductResponse.builder()
                .id(product.getId())
                .message("Product updated successfully")
                .build();

    }
    // =========================================================
    // DELETE PRODUCT
    // =========================================================

    public void deleteProduct(
            Long productId,
            Long companyId) {


        Product product =
                productRepository.findById(productId)
                        .orElseThrow(() ->
                                new ProductNotFoundException(
                                        "Product not found"
                                ));


        if (!product.getCompany()
                .getId()
                .equals(companyId)) {

            throw new RuntimeException(
                    "You cannot access this product"
            );

        }


        product.setActive(false);
        product.setDeletedAt(LocalDateTime.now());


        product.getMaterials()
                .forEach(material -> {

                    material.setActive(false);
                    material.setDeletedAt(LocalDateTime.now());

                });


        productRepository.save(product);

    }


    // =========================================================
    // PRODUCT PRICE HISTORY
    // =========================================================

    private void addNewProductPriceVersion(
            Product product,
            BigDecimal manufacturingCost,
            BigDecimal sellingPrice) {


        product.getPriceHistory()
                .stream()
                .filter(history ->
                        history.getEffectiveTo() == null)
                .findFirst()
                .ifPresent(history ->
                        history.setEffectiveTo(LocalDate.now()));


        ProductPriceHistory history =
                ProductPriceHistory.builder()
                        .product(product)
                        .manufacturingCost(manufacturingCost)
                        .sellingPrice(sellingPrice)
                        .profitMargin(
                                calculateProfitMargin(
                                        manufacturingCost,
                                        sellingPrice
                                )
                        )
                        .effectiveFrom(LocalDate.now())
                        .effectiveTo(null)
                        .build();


        product.addPriceHistory(history);

    }


    // =========================================================
    // MATERIAL SYNC
    // =========================================================

    private void syncMaterials(
            Product product,
            List<MaterialRequest> requests) {


        if (requests == null) {
            return;
        }


        Map<Long, Material> existingMaterials =
                product.getMaterials()
                        .stream()
                        .filter(material ->
                                material.getId() != null)
                        .collect(Collectors.toMap(
                                Material::getId,
                                material -> material
                        ));


        Set<Long> incomingIds = new HashSet<>();


        for (MaterialRequest request : requests) {


            // NEW MATERIAL

            if (request.getId() == null) {


                Material material = new Material();

                material.setMaterialName(request.getMaterialName());
                material.setUnit(request.getUnit());
                material.setCurrentPrice(request.getCurrentPrice());
                material.setActive(true);


                product.addMaterial(material);


                addNewMaterialPriceVersion(
                        material,
                        request.getCurrentPrice()
                );


                continue;

            }


            // EXISTING MATERIAL

            incomingIds.add(request.getId());


            Material material =
                    existingMaterials.get(request.getId());


            if (material == null) {
                continue;
            }


            boolean priceChanged =
                    material.getCurrentPrice() == null
                            ||
                            request.getCurrentPrice() == null
                            ||
                            material.getCurrentPrice()
                                    .compareTo(
                                            request.getCurrentPrice()
                                    ) != 0;


            material.setMaterialName(
                    request.getMaterialName()
            );


            material.setUnit(
                    request.getUnit()
            );


            if (priceChanged) {


                material.setCurrentPrice(
                        request.getCurrentPrice()
                );


                addNewMaterialPriceVersion(
                        material,
                        request.getCurrentPrice()
                );

            }

        }


        // SOFT DELETE REMOVED MATERIALS

        product.getMaterials()
                .stream()
                .filter(material ->
                        material.getId() != null
                                &&
                                !incomingIds.contains(material.getId()))
                .forEach(material -> {

                    material.setActive(false);
                    material.setDeletedAt(
                            LocalDateTime.now()
                    );

                });

    }


    // =========================================================
    // MATERIAL PRICE HISTORY
    // =========================================================

    private void addNewMaterialPriceVersion(
            Material material,
            BigDecimal price) {


        material.getPriceHistory()
                .stream()
                .filter(history ->
                        history.getEffectiveTo() == null)
                .findFirst()
                .ifPresent(history ->
                        history.setEffectiveTo(
                                LocalDate.now()
                        ));


        MaterialPriceHistory history =
                MaterialPriceHistory.builder()
                        .material(material)
                        .price(price)
                        .effectiveFrom(LocalDate.now())
                        .effectiveTo(null)
                        .build();


        material.addPriceHistory(history);

    }


    // =========================================================
    // PROFIT MARGIN
    // =========================================================

    private BigDecimal calculateProfitMargin(
            BigDecimal manufacturingCost,
            BigDecimal sellingPrice) {


        if (manufacturingCost == null
                ||
                manufacturingCost.compareTo(BigDecimal.ZERO) == 0) {

            return BigDecimal.ZERO;

        }


        return sellingPrice
                .subtract(manufacturingCost)
                .divide(
                        manufacturingCost,
                        4,
                        RoundingMode.HALF_UP
                )
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);

    }


    // =========================================================
    // RESPONSE MAPPER
    // =========================================================

    private ProductFullResponse mapToFullResponse(
            Product product) {


        ProductPriceHistory latestPrice =
                product.getPriceHistory()
                        .stream()
                        .filter(history ->
                                history.getEffectiveTo() == null)
                        .findFirst()
                        .orElse(null);


        List<MaterialResponse> materials =
                product.getMaterials()
                        .stream()
                        .filter(Material::getActive)
                        .map(material ->
                                MaterialResponse.builder()
                                        .materialId(material.getId())
                                        .materialName(material.getMaterialName())

                                        .unit(material.getUnit())
                                        .currentPrice(material.getCurrentPrice())
                                        .build()
                        )
                        .toList();


        return ProductFullResponse.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .hsnCode(product.getHsnCode())
                .description(product.getDescription())
                .active(product.getActive())
                .manufacturingCost(
                        latestPrice != null
                                ? latestPrice.getManufacturingCost()
                                : null
                )
                .sellingPrice(
                        latestPrice != null
                                ? latestPrice.getSellingPrice()
                                : null
                )
                .profitMargin(
                        latestPrice != null
                                ? latestPrice.getProfitMargin()
                                : null
                )
                .materials(materials)
                .build();

    }

}