package com.sleepypoem.commerceapp.utils.factories.impl;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.utils.factories.RandomGenerator;
import com.sleepypoem.commerceapp.utils.factories.abstracts.SimpleFactory;

public class UserFactory implements SimpleFactory<UserDto> {
    @Override
    public UserDto create() {
        return UserDto.builder()
                .id(RandomGenerator.randomString(10))
                .username(RandomGenerator.randomString(10))
                .firstName(RandomGenerator.randomString(10))
                .lastName(RandomGenerator.randomString(10))
                .email(RandomGenerator.randomString(10))
                .enabled(true)
                .emailVerified(true)
                .build();
    }
}
