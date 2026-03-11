package com.challenge.product.catalog.service.domain.service;

import com.challenge.product.catalog.service.domain.model.Product;

import java.math.BigDecimal;

public class ElectronicsDiscount implements DiscountCalculatorStrategy {

    @Override
    public boolean isApplicable(Product product) {
        return product.getCategory().getName().equals("Electronics");
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(0.15));
    }
}