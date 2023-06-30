package com.sleepypoem.commerceapp.utils.factories.impl;

import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.domain.enums.Currency;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.utils.factories.RandomGenerator;
import com.sleepypoem.commerceapp.utils.factories.abstracts.SimpleFactory;

import java.time.LocalDateTime;

public class PaymentFactory implements SimpleFactory<PaymentEntity> {
    @Override
    public PaymentEntity create() {
        CheckoutFactory checkoutFactory = new CheckoutFactory();
        return PaymentEntity.builder()
                .id(RandomGenerator.randomLong(0, 9999))
                .currency(Currency.USD)
                .paymentProviderMessage(RandomGenerator.randomString(5))
                .userId(RandomGenerator.randomString(5))
                .checkout(checkoutFactory.create())
                .createdAt(RandomGenerator.randomPastDate())
                .updatedAt(LocalDateTime.now())
                .status(PaymentStatus.PROCESSING)
                .build();
    }

    public PaymentEntity createWithUserId(String userId) {
        CheckoutFactory checkoutFactory = new CheckoutFactory();
        return PaymentEntity.builder()
                .id(RandomGenerator.randomLong(0, 9999))
                .currency(Currency.USD)
                .paymentProviderMessage(RandomGenerator.randomString(5))
                .userId(userId)
                .checkout(checkoutFactory.createWithUserId(userId))
                .createdAt(RandomGenerator.randomPastDate())
                .updatedAt(LocalDateTime.now())
                .status(PaymentStatus.PROCESSING)
                .build();
    }
}
