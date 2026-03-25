package com.challenge.product.catalog.service.infrastructure.controller;

import com.challenge.product.catalog.service.application.exception.InvalidCategoryException;
import com.challenge.product.catalog.service.application.exception.RepositoryException;
import com.challenge.product.catalog.service.application.port.in.GetProductCatalogUseCase;
import com.challenge.product.catalog.service.domain.model.Product;
import com.challenge.product.catalog.service.infrastructure.controller.dto.ProductResponseDto;
import com.challenge.product.catalog.service.infrastructure.mapper.ProductCatalogMapper;
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
class ProductCatalogControllerTest {

    @InjectMocks
    private ProductCatalogController productCatalogController;

    @Mock
    private GetProductCatalogUseCase getProductCatalogUseCase;

    @Mock
    private ProductCatalogMapper productCatalogMapper;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    @DisplayName("Test getProductCatalog WHEN called with no category THEN returns mapped page of ProductResponseDto.")
    void getProductCatalog_noCategory_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product = easyRandom.nextObject(Product.class);
        ProductResponseDto productResponseDto = easyRandom.nextObject(ProductResponseDto.class);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(getProductCatalogUseCase.getProductCatalog(null, pageable)).thenReturn(productPage);
        when(productCatalogMapper.toDTO(product)).thenReturn(productResponseDto);

        Page<ProductResponseDto> result = productCatalogController.getProductCatalog(null, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(productResponseDto, result.getContent().get(0));
        verify(getProductCatalogUseCase, times(1)).getProductCatalog(null, pageable);
        verify(productCatalogMapper, times(1)).toDTO(product);
    }

    @Test
    @DisplayName("Test getProductCatalog WHEN called with a category THEN returns mapped page of ProductResponseDto.")
    void getProductCatalog_withCategory_returnsMappedPage() {
        String category = "Electronics";
        Pageable pageable = PageRequest.of(0, 10);
        Product product = easyRandom.nextObject(Product.class);
        ProductResponseDto productResponseDto = easyRandom.nextObject(ProductResponseDto.class);
        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(getProductCatalogUseCase.getProductCatalog(category, pageable)).thenReturn(productPage);
        when(productCatalogMapper.toDTO(product)).thenReturn(productResponseDto);

        Page<ProductResponseDto> result = productCatalogController.getProductCatalog(category, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(productResponseDto, result.getContent().get(0));
        verify(getProductCatalogUseCase, times(1)).getProductCatalog(category, pageable);
        verify(productCatalogMapper, times(1)).toDTO(product);
    }

    @Test
    @DisplayName("Test getProductCatalog WHEN use case throws InvalidCategoryException THEN propagates exception.")
    void getProductCatalog_invalidCategory_propagatesInvalidCategoryException() {
        String category = "NonExistent";
        Pageable pageable = PageRequest.of(0, 10);
        String expectedMessage = "Invalid or unknown category - category: " + category;

        when(getProductCatalogUseCase.getProductCatalog(category, pageable))
                .thenThrow(new InvalidCategoryException(expectedMessage));

        InvalidCategoryException exception = assertThrows(InvalidCategoryException.class, () ->
                productCatalogController.getProductCatalog(category, pageable));

        assertEquals(expectedMessage, exception.getMessage());
        verify(getProductCatalogUseCase, times(1)).getProductCatalog(category, pageable);
        verify(productCatalogMapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Test getProductCatalog WHEN use case throws RepositoryException THEN propagates exception.")
    void getProductCatalog_repositoryFailure_propagatesRepositoryException() {
        String category = "Electronics";
        Pageable pageable = PageRequest.of(0, 10);
        String errorMessage = "Simulated DB error";
        String expectedMessage = "Error retrieving product catalog - category: " + category + " - error: " + errorMessage;

        when(getProductCatalogUseCase.getProductCatalog(category, pageable))
                .thenThrow(new RepositoryException(expectedMessage, new RuntimeException(errorMessage)));

        RepositoryException exception = assertThrows(RepositoryException.class, () ->
                productCatalogController.getProductCatalog(category, pageable));

        assertEquals(expectedMessage, exception.getMessage());
        verify(getProductCatalogUseCase, times(1)).getProductCatalog(category, pageable);
        verify(productCatalogMapper, never()).toDTO(any());
    }
}