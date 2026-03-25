package com.challenge.product.catalog.service.infrastructure.mapper;

import com.challenge.product.catalog.service.domain.model.Category;
import com.challenge.product.catalog.service.domain.model.Product;
import com.challenge.product.catalog.service.infrastructure.controller.dto.ProductResponseDto;
import com.challenge.product.catalog.service.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductCatalogMapper {

    @Named("categoryToString")
    default String categoryToString(Category category) {
        return category != null ? category.getName() : null;
    }

    @Mapping(target = "category", source = "category", qualifiedByName = "categoryToString")
    ProductResponseDto toDTO(Product product);

    @Mapping(source = "price", target = "listPrice")
    Product toModel(ProductEntity productEntity);

}
