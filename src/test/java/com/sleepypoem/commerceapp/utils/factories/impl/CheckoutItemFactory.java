package com.sleepypoem.commerceapp.utils.factories.impl;

import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.utils.factories.RandomGenerator;
import com.sleepypoem.commerceapp.utils.factories.abstracts.SimpleFactory;

public class CheckoutItemFactory implements SimpleFactory<CheckoutItemEntity> {


    @Override
    public CheckoutItemEntity create() {
        ProductFactory productFactory = new ProductFactory();
        return CheckoutItemEntity.builder()
                .id(RandomGenerator.randomLong(0, 9999))
                .product(productFactory.create())
                .quantity(RandomGenerator.randomInt(1, 10))
                .build();
    }
}
