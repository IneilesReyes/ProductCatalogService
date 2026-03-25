package com.challenge.product.catalog.service.domain.service;

import com.challenge.product.catalog.service.domain.model.Product;
import com.challenge.product.catalog.service.testUtils.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class ElectronicsDiscountTest {

    private final ElectronicsDiscount electronicsDiscount = new ElectronicsDiscount();

    @Test
    @DisplayName("Test isApplicable WHEN product category is Electronics THEN returns true.")
    void isApplicable_electronicsCategory_returnsTrue() {
        Product product = Utils.buildProduct("SKU0001", "Wireless Mouse with ergonomic design", 19.99, "Electronics");

        assertTrue(electronicsDiscount.isApplicable(product));
    }

    @Test
    @DisplayName("Test isApplicable WHEN product category is not Electronics THEN returns false.")
    void isApplicable_nonElectronicsCategory_returnsFalse() {
        Product product = Utils.buildProduct("SKU0008", "Yoga Mat with Non-Slip Surface", 35.00, "Sports");

        assertFalse(electronicsDiscount.isApplicable(product));
    }

    @Test
    @DisplayName("Test calculateDiscount WHEN called THEN returns 15% of the price.")
    void calculateDiscount_returns15PercentOfPrice() {
        BigDecimal price = BigDecimal.valueOf(120.00);

        BigDecimal result = electronicsDiscount.calculateDiscount(price);

        assertEquals(0, result.compareTo(BigDecimal.valueOf(18.00)));
    }

}
