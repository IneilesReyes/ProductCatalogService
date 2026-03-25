package com.challenge.product.catalog.service.application.service;

import com.challenge.product.catalog.service.application.exception.InvalidCategoryException;
import com.challenge.product.catalog.service.application.exception.RepositoryException;
import com.challenge.product.catalog.service.application.port.out.CategoryPort;
import com.challenge.product.catalog.service.application.port.out.ProductCatalogPort;
import com.challenge.product.catalog.service.domain.model.Product;
import com.challenge.product.catalog.service.domain.service.DiscountSelector;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductCatalogServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductCatalogPort productCatalogPort;

    @Mock
    private CategoryPort categoryPort;

    @Mock
    private DiscountSelector discountSelector;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    @DisplayName("Test getProductCatalog WHEN category is null THEN skips validation and returns mapped page with discounts applied.")
    void getProductCatalog_nullCategory_returnsDiscountedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = easyRandom.nextObject(Product.class);
        Product discountedProduct = easyRandom.nextObject(Product.class);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productCatalogPort.findAll(pageable)).thenReturn(productPage);
        when(discountSelector.calculateDiscount(product)).thenReturn(discountedProduct);

        Page<Product> result = productService.getProductCatalog(null, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(discountedProduct, result.getContent().get(0));
        verify(categoryPort, never()).exists(any());
        verify(productCatalogPort, never()).findByCategoryName(any(), any());
        verify(productCatalogPort, times(1)).findAll(pageable);
        verify(discountSelector, times(1)).calculateDiscount(product);
    }

    @Test
    @DisplayName("Test getProductCatalog WHEN category is valid THEN validates, fetches, and returns page with discounts applied.")
    void getProductCatalog_validCategory_returnsDiscountedPage() {
        String category = "Electronics";
        Pageable pageable = PageRequest.of(0, 10);
        Product product = easyRandom.nextObject(Product.class);
        Product discountedProduct = easyRandom.nextObject(Product.class);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(categoryPort.exists(category)).thenReturn(true);
        when(productCatalogPort.findByCategoryName(category, pageable)).thenReturn(productPage);
        when(discountSelector.calculateDiscount(product)).thenReturn(discountedProduct);

        Page<Product> result = productService.getProductCatalog(category, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(discountedProduct, result.getContent().get(0));
        verify(productCatalogPort, never()).findAll(any());
        verify(categoryPort, times(1)).exists(category);
        verify(productCatalogPort, times(1)).findByCategoryName(category, pageable);
        verify(discountSelector, times(1)).calculateDiscount(product);
    }

    @Test
    @DisplayName("Test getProductCatalog WHEN category is invalid THEN throws InvalidCategoryException.")
    void getProductCatalog_invalidCategory_throwsInvalidCategoryException() {
        String category = "NonExistent";
        Pageable pageable = PageRequest.of(0, 10);

        when(categoryPort.exists(category)).thenReturn(false);

        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () ->
                productService.getProductCatalog(category, pageable));

        assertTrue(exception.getMessage().contains("Invalid or unknown category"));
        assertTrue(exception.getMessage().contains("category: " + category));
        verify(categoryPort, times(1)).exists(category);
        verify(productCatalogPort, never()).findByCategoryName(any(), any());
        verify(discountSelector, never()).calculateDiscount(any());
    }

    @Test
    @DisplayName("Test getProductCatalog WHEN repository throws exception THEN throws RepositoryException.")
    void getProductCatalog_repositoryFailure_throwsRepositoryException() {
        String category = "Electronics";
        Pageable pageable = PageRequest.of(0, 10);
        String errorMessage = "Simulated DB error";
        String expectedMessage = "Error retrieving product catalog - category: %s - error: %s"
                .formatted(category, errorMessage);

        when(categoryPort.exists(category)).thenReturn(true);
        when(productCatalogPort.findByCategoryName(category, pageable))
                .thenThrow(new RuntimeException(errorMessage));

        RepositoryException exception = assertThrows(RepositoryException.class, () ->
                productService.getProductCatalog(category, pageable));

        assertEquals(expectedMessage, exception.getMessage());
        verify(categoryPort, times(1)).exists(category);
        verify(productCatalogPort, times(1)).findByCategoryName(category, pageable);
        verify(discountSelector, never()).calculateDiscount(any());
    }
}