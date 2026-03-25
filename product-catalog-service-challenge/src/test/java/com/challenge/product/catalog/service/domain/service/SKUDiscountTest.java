package com.challenge.product.catalog.service.domain.service;

import com.challenge.product.catalog.service.domain.model.Product;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SKUDiscountTest {

    private final EasyRandom easyRandom = new EasyRandom();
    private final SKUDiscount skuDiscount = new SKUDiscount();

    @Test
    @DisplayName("Test isApplicable WHEN product SKU ends with 5 THEN returns true.")
    void isApplicable_skuEndingWith5_returnsTrue() {
        Product product = Utils.buildProduct("SKU0005", "Noise-Cancelling Over-Ear Headphones", 120.00, "Electronics");

        assertTrue(skuDiscount.isApplicable(product));
    }

    @Test
    @DisplayName("Test isApplicable WHEN product SKU does not end with 5 THEN returns false.")
    void isApplicable_skuNotEndingWith5_returnsFalse() {
        Product product = Utils.buildProduct("SKU0001", "Wireless Mouse with ergonomic design", 19.99, "Electronics");

        assertFalse(skuDiscount.isApplicable(product));
    }

    @Test
    @DisplayName("Test calculateDiscount WHEN called THEN returns 30% of the price.")
    void calculateDiscount_returns30PercentOfPrice() {
        BigDecimal price = BigDecimal.valueOf(120.00);

        BigDecimal result = skuDiscount.calculateDiscount(price);

        assertEquals(0, result.compareTo(BigDecimal.valueOf(36.00)));
    }

}
