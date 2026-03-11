package com.challenge.product.catalog.service.infrastructure.controller;

import com.challenge.product.catalog.service.application.port.in.GetProductCatalogUseCase;
import com.challenge.product.catalog.service.infrastructure.controller.dto.ProductResponseDto;
import com.challenge.product.catalog.service.infrastructure.mapper.ProductCatalogMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/products")
public class ProductCatalogController {

    private final GetProductCatalogUseCase getProductCatalogUseCase;
    private final ProductCatalogMapper productCatalogMapper;

    public ProductCatalogController(GetProductCatalogUseCase getProductCatalogUseCase,
                                    ProductCatalogMapper productCatalogMapper) {
        this.getProductCatalogUseCase = getProductCatalogUseCase;
        this.productCatalogMapper = productCatalogMapper;
    }


    @GetMapping
    public Page<ProductResponseDto> getProductCatalog(@RequestParam(required = false) String category, Pageable pageable) {
        return getProductCatalogUseCase.getProductCatalog(category, pageable)
                .map(productCatalogMapper::toDTO);
    }
}
