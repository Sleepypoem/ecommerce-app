package com.sleepypoem.commerceapp.utils.factories.impl;

import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.utils.factories.RandomGenerator;
import com.sleepypoem.commerceapp.utils.factories.abstracts.SimpleFactory;

import java.time.LocalDateTime;

public class CheckoutFactory implements SimpleFactory<CheckoutEntity> {
    @Override
    public CheckoutEntity create() {
        AddressFactory addressFactory = new AddressFactory();
        PaymentMethodFactory paymentMethodFactory = new PaymentMethodFactory();
        CheckoutItemFactory checkoutItemFactory = new CheckoutItemFactory();
        return CheckoutEntity.builder()
                .id(RandomGenerator.randomLong(0, 9999))
                .userId(RandomGenerator.randomString(5))
                .address(addressFactory.create())
                .paymentMethod(paymentMethodFactory.create())
                .items(checkoutItemFactory.createList(1))
                .createdAt(RandomGenerator.randomPastDate())
                .updatedAt(LocalDateTime.now())
                .status(CheckoutStatus.PENDING)
                .build();
    }

    public CheckoutEntity createWithUserId(String userId) {
        AddressFactory addressFactory = new AddressFactory();
        PaymentMethodFactory paymentMethodFactory = new PaymentMethodFactory();
        CheckoutItemFactory checkoutItemFactory = new CheckoutItemFactory();
        return CheckoutEntity.builder()
                .id(RandomGenerator.randomLong(0, 9999))
                .userId(userId)
                .address(addressFactory.createWithUserId(userId))
                .paymentMethod(paymentMethodFactory.createWithUserId(userId))
                .items(checkoutItemFactory.createList(1))
                .createdAt(RandomGenerator.randomPastDate())
                .updatedAt(LocalDateTime.now())
                .status(CheckoutStatus.PENDING)
                .build();
    }
}
