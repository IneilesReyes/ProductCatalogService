package com.challenge.product.catalog.service.infrastructure.configuration;

import com.challenge.product.catalog.service.application.port.in.GetProductCatalogUseCase;
import com.challenge.product.catalog.service.application.port.out.CategoryPort;
import com.challenge.product.catalog.service.application.port.out.ProductCatalogPort;
import com.challenge.product.catalog.service.application.service.ProductService;
import com.challenge.product.catalog.service.domain.service.*;
import com.challenge.product.catalog.service.infrastructure.mapper.ProductCatalogMapper;
import com.challenge.product.catalog.service.infrastructure.persistence.JpaProductCatalogAdapter;
import com.challenge.product.catalog.service.infrastructure.persistence.ProductCatalogDao;
import com.challenge.product.catalog.service.infrastructure.persistence.entity.CategoryDao;
import com.challenge.product.catalog.service.infrastructure.persistence.entity.JpaCategoryAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ServiceConfig {

    @Bean
    public DiscountCalculatorStrategy electronicDiscount(){
        return new ElectronicsDiscount();
    }

    @Bean
    public DiscountCalculatorStrategy homeAndKitchenDiscount(){
        return new HomeAndKitchenDiscount();
    }

    @Bean
    public DiscountCalculatorStrategy SKUDiscount(){
        return new SKUDiscount();
    }

    @Bean
    public DiscountSelector discountSelector(List<DiscountCalculatorStrategy> discountCalculators){
        return new DiscountSelector(discountCalculators);
    }

    @Bean
    public ProductCatalogPort jpaProductCatalog(ProductCatalogDao productCatalogDao, ProductCatalogMapper productCatalogMapper){
        return new JpaProductCatalogAdapter(productCatalogDao, productCatalogMapper);
    }

    @Bean
    public CategoryPort jpaCategory(CategoryDao categoryDao){
        return new JpaCategoryAdapter(categoryDao);
    }

    @Bean
    public GetProductCatalogUseCase productService(ProductCatalogPort productCatalogPort, CategoryPort categoryPort, DiscountSelector discountSelector){
        return new ProductService(productCatalogPort, categoryPort, discountSelector);
    }

}
