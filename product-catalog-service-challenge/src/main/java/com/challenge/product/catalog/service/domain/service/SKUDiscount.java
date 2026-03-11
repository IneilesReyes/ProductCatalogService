package com.challenge.product.catalog.service.domain.service;

import com.challenge.product.catalog.service.domain.model.Product;

import java.math.BigDecimal;

public class SKUDiscount implements DiscountCalculatorStrategy {

    @Override
    public boolean isApplicable(Product product) {
        return product.getSKU().endsWith("5");
    }

    @Override
    public BigDecimal calculateDiscount(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(0.3));
    }

}