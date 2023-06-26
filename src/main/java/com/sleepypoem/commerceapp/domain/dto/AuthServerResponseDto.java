package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sleepypoem.commerceapp.config.beans.GsonProvider;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthServerResponseDto {

    private String accessToken;
    private String refreshToken;
    private String expiresIn;
    private String refreshExpiresIn;
    private Long issuedAt;

    public String toJsonString() {
        return GsonProvider.getGson().toJson(this);
    }
    public static AuthServerResponseDto fromJsonString(String jsonString) {
        return GsonProvider.getGson().fromJson(jsonString, AuthServerResponseDto.class);
    }
}
