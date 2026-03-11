package com.challenge.product.catalog.service.infrastructure.controller.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder(toBuilder = true)
public class ProductResponseDto {
    private String SKU;
    private String description;
    private BigDecimal listPrice;
    private String category;
    private String discountApplied;
    private BigDecimal finalPrice;

}
