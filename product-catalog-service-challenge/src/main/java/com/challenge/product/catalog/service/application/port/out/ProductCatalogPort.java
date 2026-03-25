package com.challenge.product.catalog.service.application.port.out;

import com.challenge.product.catalog.service.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCatalogPort {

    Page<Product> findByCategoryName(String category, Pageable pageable);
    Page<Product> findAll(Pageable pageable);

}
