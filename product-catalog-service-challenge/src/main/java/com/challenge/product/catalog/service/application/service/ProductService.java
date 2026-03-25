package com.challenge.product.catalog.service.application.service;

import com.challenge.product.catalog.service.application.exception.InvalidCategoryException;
import com.challenge.product.catalog.service.application.exception.RepositoryException;
import com.challenge.product.catalog.service.application.port.in.GetProductCatalogUseCase;
import com.challenge.product.catalog.service.application.port.out.CategoryPort;
import com.challenge.product.catalog.service.application.port.out.ProductCatalogPort;
import com.challenge.product.catalog.service.domain.model.Product;
import com.challenge.product.catalog.service.domain.service.DiscountSelector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class ProductService implements GetProductCatalogUseCase {

    private static final String FETCH_ERROR_TEMPLATE = "category: %s";

    private final ProductCatalogPort productCatalogPort;
    private final CategoryPort categoryPort;
    private final DiscountSelector discountSelector;

    public ProductService(ProductCatalogPort productCatalogPort, CategoryPort categoryPort, DiscountSelector discountSelector) {
        this.productCatalogPort = productCatalogPort;
        this.categoryPort = categoryPort;
        this.discountSelector = discountSelector;
    }

    @Override
    public Page<Product> getProductCatalog(String category, Pageable pageable) {
        validateCategoryIfPresent(category);
        return fetchProducts(category, pageable)
                .map(discountSelector::calculateDiscount);
    }

    private void validateCategoryIfPresent(String category) {
        if (category != null && !categoryPort.exists(category)) {
            throw new InvalidCategoryException(
                    "Invalid or unknown category - " + FETCH_ERROR_TEMPLATE.formatted(category));
        }
    }

    private Page<Product> fetchProducts(String category, Pageable pageable) {
        try {
            return Optional.ofNullable(category)
                    .map(cat -> productCatalogPort.findByCategoryName(cat, pageable))
                    .orElseGet(() -> productCatalogPort.findAll(pageable));
        } catch (Exception e) {
            throw new RepositoryException(
                    "Error retrieving product catalog - " +
                            FETCH_ERROR_TEMPLATE.formatted(category) +
                            " - error: " + e.getMessage(),
                    e
            );
        }
    }
}