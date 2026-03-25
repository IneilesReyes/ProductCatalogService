package com.challenge.product.catalog.service.domain.service;

import com.challenge.product.catalog.service.domain.model.Product;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountSelectorTest {

    private final EasyRandom easyRandom = new EasyRandom();

    @Mock
    private DiscountCalculatorStrategy strategyMock;

    private DiscountSelector discountSelector;

    @Test
    @DisplayName("Test calculateDiscount WHEN one strategy is applicable THEN returns product with discount applied.")
    void calculateDiscount_oneApplicableStrategy_returnsDiscountedProduct() {
        Product product = easyRandom.nextObject(Product.class);
        BigDecimal discount = BigDecimal.valueOf(10.00);

        discountSelector = new DiscountSelector(List.of(strategyMock));

        when(strategyMock.isApplicable(product)).thenReturn(true);
        when(strategyMock.calculateDiscount(product.getListPrice())).thenReturn(discount);

        Product result = discountSelector.calculateDiscount(product);

        assertEquals(product.getListPrice().subtract(discount), result.getFinalPrice());
        assertEquals(discount, result.getDiscountApplied());
        verify(strategyMock, times(1)).isApplicable(product);
        verify(strategyMock, times(1)).calculateDiscount(product.getListPrice());
    }

    @Test
    @DisplayName("Test calculateDiscount WHEN no strategy is applicable THEN returns product with zero discount.")
    void calculateDiscount_noApplicableStrategy_returnsZeroDiscount() {
        Product product = easyRandom.nextObject(Product.class);

        discountSelector = new DiscountSelector(List.of(strategyMock));

        when(strategyMock.isApplicable(product)).thenReturn(false);

        Product result = discountSelector.calculateDiscount(product);

        assertEquals(product.getListPrice(), result.getFinalPrice());
        assertEquals(BigDecimal.ZERO, result.getDiscountApplied());
        verify(strategyMock, times(1)).isApplicable(product);
        verify(strategyMock, never()).calculateDiscount(any());
    }

    @Test
    @DisplayName("Test calculateDiscount WHEN multiple strategies are applicable THEN returns product with the highest discount applied.")
    void calculateDiscount_multipleApplicableStrategies_returnsHighestDiscount() {
        Product product = easyRandom.nextObject(Product.class);
        BigDecimal lowerDiscount = BigDecimal.valueOf(5.00);
        BigDecimal higherDiscount = BigDecimal.valueOf(20.00);

        DiscountCalculatorStrategy secondStrategyMock = mock(DiscountCalculatorStrategy.class);
        discountSelector = new DiscountSelector(List.of(strategyMock, secondStrategyMock));

        when(strategyMock.isApplicable(product)).thenReturn(true);
        when(strategyMock.calculateDiscount(product.getListPrice())).thenReturn(lowerDiscount);
        when(secondStrategyMock.isApplicable(product)).thenReturn(true);
        when(secondStrategyMock.calculateDiscount(product.getListPrice())).thenReturn(higherDiscount);

        Product result = discountSelector.calculateDiscount(product);

        assertEquals(product.getListPrice().subtract(higherDiscount), result.getFinalPrice());
        assertEquals(higherDiscount, result.getDiscountApplied());
    }
}