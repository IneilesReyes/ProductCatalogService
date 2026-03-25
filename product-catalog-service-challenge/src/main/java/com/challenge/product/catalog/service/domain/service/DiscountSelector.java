package com.challenge.product.catalog.service.domain.service;

import com.challenge.product.catalog.service.domain.model.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class DiscountSelector {

    private final List<DiscountCalculatorStrategy> discountCalculators;

    public DiscountSelector(List<DiscountCalculatorStrategy> discountCalculators) {
        this.discountCalculators = discountCalculators;
    }

    public Product calculateDiscount(Product product) {
        BigDecimal discount = discountCalculators.stream()
                .filter(dc -> dc.isApplicable(product))
                .map(dc -> dc.calculateDiscount(product.getListPrice()))
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        return product.toBuilder()
                .finalPrice(product.getListPrice().subtract(discount).setScale(2, RoundingMode.HALF_DOWN))
                .discountApplied(discount)
                .build();
    }

}
