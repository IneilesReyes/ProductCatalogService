package com.challenge.product.catalog.service.infrastructure.persistence.entity;

import com.challenge.product.catalog.service.application.port.out.CategoryPort;

public class JpaCategoryAdapter implements CategoryPort {

    private CategoryDao categoryDao;

    public JpaCategoryAdapter(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public Boolean exists(String category) {
        return categoryDao.existsByName(category);
    }

}
