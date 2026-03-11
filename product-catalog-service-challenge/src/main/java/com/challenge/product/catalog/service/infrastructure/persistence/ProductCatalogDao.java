package com.challenge.product.catalog.service.infrastructure.persistence;

import com.challenge.product.catalog.service.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCatalogDao extends JpaRepository<ProductEntity, Long> {

    Page<ProductEntity> findByCategoryName(String category, Pageable pageable);

}
