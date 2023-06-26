package com.sleepypoem.commerceapp.utils.factories.impl;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.utils.factories.RandomGenerator;
import com.sleepypoem.commerceapp.utils.factories.abstracts.SimpleFactory;

import java.time.LocalDateTime;

public class ProductFactory implements SimpleFactory<ProductEntity> {
    @Override
    public ProductEntity create() {
        return ProductEntity.builder()
                .id(RandomGenerator.randomLong(0, 9999))
                .name(RandomGenerator.randomString(5))
                .price(RandomGenerator.randomDouble(0.1, 99.99))
                .stock(RandomGenerator.randomInt(50, 100))
                .description(RandomGenerator.randomString(10))
                .createdAt(RandomGenerator.randomPastDate())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
