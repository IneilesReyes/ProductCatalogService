package com.challenge.product.catalog.service.application.port.in;

import com.challenge.product.catalog.service.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetProductCatalogUseCase {

    Page<Product> getProductCatalog(String category, Pageable pageable);

}
