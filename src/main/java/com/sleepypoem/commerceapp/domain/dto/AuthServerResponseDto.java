package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthServerResponseDto {

    private String accessToken;
    private String refreshToken;
    private String expiresIn;
    private String refreshExpiresIn;
}
