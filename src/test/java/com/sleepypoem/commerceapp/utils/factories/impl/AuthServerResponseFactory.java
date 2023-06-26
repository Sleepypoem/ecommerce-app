package com.sleepypoem.commerceapp.utils.factories.impl;

import com.sleepypoem.commerceapp.domain.dto.AuthServerResponseDto;
import com.sleepypoem.commerceapp.utils.factories.abstracts.SimpleFactory;

public class AuthServerResponseFactory implements SimpleFactory<AuthServerResponseDto> {
    @Override
    public AuthServerResponseDto create() {
        AuthServerResponseDto authServerResponseDto = new AuthServerResponseDto();
        authServerResponseDto.setAccessToken("access_token");
        authServerResponseDto.setRefreshToken("refresh_token");
        authServerResponseDto.setExpiresIn(String.valueOf(1800));
        authServerResponseDto.setRefreshExpiresIn(String.valueOf(3600));
        authServerResponseDto.setIssuedAt(System.currentTimeMillis());

        return authServerResponseDto;
    }

    public AuthServerResponseDto createWithExpiredToken() {
        AuthServerResponseDto authServerResponseDto = create();
        authServerResponseDto.setIssuedAt(System.currentTimeMillis() - 1900);

        return authServerResponseDto;
    }
}
