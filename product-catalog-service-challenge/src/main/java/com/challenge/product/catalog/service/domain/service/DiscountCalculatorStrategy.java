package com.challenge.product.catalog.service.domain.service;

import com.challenge.product.catalog.service.domain.model.Product;

import java.math.BigDecimal;

public interface DiscountCalculatorStrategy {

    boolean isApplicable(Product product);
    BigDecimal calculateDiscount(BigDecimal price);

}
