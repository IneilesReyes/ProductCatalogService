package com.challenge.product.catalog.service.infrastructure.persistence.entity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<CategoryEntity, Long> {
    Boolean existsByName(String name);
}
