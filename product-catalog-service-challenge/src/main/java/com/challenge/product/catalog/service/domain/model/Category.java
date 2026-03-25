package com.challenge.product.catalog.service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder(toBuilder = true)
public class Category {
    private Long id;
    private String name;

}
