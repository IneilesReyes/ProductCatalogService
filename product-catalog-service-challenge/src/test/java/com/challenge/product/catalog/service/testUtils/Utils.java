package com.challenge.product.catalog.service.testUtils;

import com.challenge.product.catalog.service.domain.model.Category;
import com.challenge.product.catalog.service.domain.model.Product;
import org.jeasy.random.EasyRandom;

import java.math.BigDecimal;

public class Utils {

    private static final EasyRandom easyRandom = new EasyRandom();

    public static Product buildProduct(String sku, String description, double listPrice, String categoryName) {
        return easyRandom.nextObject(Product.class).toBuilder()
                .SKU(sku)
                .description(description)
                .listPrice(BigDecimal.valueOf(listPrice))
                .category(Category.builder().name(categoryName).build())
                .build();
    }
}
