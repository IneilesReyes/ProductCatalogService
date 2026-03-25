package com.challenge.product.catalog.service.domain.service;

import com.challenge.product.catalog.service.domain.model.Product;
import com.challenge.product.catalog.service.testUtils.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class HomeAndKitchenDiscountTest {

    private final HomeAndKitchenDiscount homeAndKitchenDiscount = new HomeAndKitchenDiscount();

    @Test
    @DisplayName("Test isApplicable WHEN product category is Home & Kitchen THEN returns true.")
    void isApplicable_homeAndKitchenCategory_returnsTrue() {
        Product product = Utils.buildProduct("SKU0003", "Stainless Steel Water Bottle, 1L", 29.50, "Home & Kitchen");

        assertTrue(homeAndKitchenDiscount.isApplicable(product));
    }

    @Test
    @DisplayName("Test isApplicable WHEN product category is not Home & Kitchen THEN returns false.")
    void isApplicable_nonHomeAndKitchenCategory_returnsFalse() {
        Product product = Utils.buildProduct("SKU0004", "Cotton T-Shirt, Unisex, Size M", 15.00, "Clothing");

        assertFalse(homeAndKitchenDiscount.isApplicable(product));
    }

    @Test
    @DisplayName("Test calculateDiscount WHEN called THEN returns 25% of the price.")
    void calculateDiscount_returns25PercentOfPrice() {
        BigDecimal price = BigDecimal.valueOf(29.50);

        BigDecimal result = homeAndKitchenDiscount.calculateDiscount(price);

        assertEquals(BigDecimal.valueOf(7.375), result);
    }

}
