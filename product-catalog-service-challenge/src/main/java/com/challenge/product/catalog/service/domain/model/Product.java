package com.challenge.product.catalog.service.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder(toBuilder = true)
public class Product {
    Long id;
    private String description;
    private Category category;
    private String SKU;
    private BigDecimal listPrice;
    private BigDecimal discountApplied;
    private BigDecimal finalPrice;

}
