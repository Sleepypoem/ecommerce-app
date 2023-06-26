package com.sleepypoem.commerceapp.utils.factories.impl;

import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.utils.factories.RandomGenerator;
import com.sleepypoem.commerceapp.utils.factories.abstracts.SimpleFactory;

import java.time.LocalDateTime;

public class PaymentMethodFactory implements SimpleFactory<PaymentMethodEntity> {
    @Override
    public PaymentMethodEntity create() {
        return PaymentMethodEntity.builder()
                .id(RandomGenerator.randomLong(0, 9999))
                .userId(RandomGenerator.randomString(5))
                .paymentId(RandomGenerator.randomString(5))
                .paymentType(RandomGenerator.randomString(5))
                .stripeUserId(RandomGenerator.randomString(5))
                .brand(RandomGenerator.randomString(5))
                .last4(RandomGenerator.randomString(4))
                .expMonth(RandomGenerator.randomString(2))
                .expYear(RandomGenerator.randomString(2))
                .createdAt(RandomGenerator.randomPastDate())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public PaymentMethodEntity createWithUserId(String userId) {
        return PaymentMethodEntity.builder()
                .id(RandomGenerator.randomLong(0, 9999))
                .userId(userId)
                .paymentId(RandomGenerator.randomString(5))
                .paymentType(RandomGenerator.randomString(5))
                .stripeUserId(RandomGenerator.randomString(5))
                .brand(RandomGenerator.randomString(5))
                .last4(RandomGenerator.randomString(4))
                .expMonth(RandomGenerator.randomString(2))
                .expYear(RandomGenerator.randomString(2))
                .createdAt(RandomGenerator.randomPastDate())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
