package com.challenge.product.catalog.service.infrastructure.persistence;

import com.challenge.product.catalog.service.domain.model.Product;
import com.challenge.product.catalog.service.application.port.out.ProductCatalogPort;
import com.challenge.product.catalog.service.infrastructure.mapper.ProductCatalogMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public class JpaProductCatalogAdapter implements ProductCatalogPort {

    private ProductCatalogDao productCatalogDao;
    private ProductCatalogMapper productCatalogMapper;

    public JpaProductCatalogAdapter(ProductCatalogDao productCatalogDao, ProductCatalogMapper productCatalogMapper) {
        this.productCatalogDao = productCatalogDao;
        this.productCatalogMapper = productCatalogMapper;
    }

    @Override
    public Page<Product> findByCategoryName(String category, Pageable pageable) {
        return Optional.ofNullable(category)
                .map(cat -> productCatalogDao.findByCategoryName(cat, pageable))
                .orElseGet(() -> productCatalogDao.findAll(pageable))
                .map(productCatalogMapper::toModel);
    }
}
