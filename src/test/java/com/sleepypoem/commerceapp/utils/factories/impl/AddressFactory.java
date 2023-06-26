package com.sleepypoem.commerceapp.utils.factories.impl;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.utils.factories.RandomGenerator;
import com.sleepypoem.commerceapp.utils.factories.abstracts.SimpleFactory;

import java.time.LocalDateTime;

public class AddressFactory implements SimpleFactory<AddressEntity> {
    @Override
    public AddressEntity create() {
        return AddressEntity.builder()
                .userId(RandomGenerator.randomString(5))
                .id(RandomGenerator.randomLong(0, 9999))
                .firstLine(RandomGenerator.randomString(12))
                .secondLine(RandomGenerator.randomString(12))
                .country(RandomGenerator.randomString(8))
                .state(RandomGenerator.randomString(8))
                .zipCode(RandomGenerator.randomString(4))
                .createdAt(LocalDateTime.of(2022, 6, 12, 23, 45))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public AddressEntity createWithUserId(String userId) {
        return AddressEntity.builder()
                .userId(userId)
                .id(RandomGenerator.randomLong(0, 9999))
                .firstLine(RandomGenerator.randomString(12))
                .secondLine(RandomGenerator.randomString(12))
                .country(RandomGenerator.randomString(8))
                .state(RandomGenerator.randomString(8))
                .zipCode(RandomGenerator.randomString(4))
                .createdAt(LocalDateTime.of(2022, 6, 12, 23, 45))
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
