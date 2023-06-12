package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sleepypoem.commerceapp.config.beans.ApplicationContextProvider;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthServerResponseDto {

    private String accessToken;
    private String refreshToken;
    private String expiresIn;
    private String refreshExpiresIn;
    private Long issuedAt;

    public static AuthServerResponseDto fromJsonString(String jsonString) {
        ObjectMapper mapper = ApplicationContextProvider.applicationContext.getBean(ObjectMapper.class);
        try {
            return mapper.readValue(jsonString, AuthServerResponseDto.class);
        } catch (Exception e) {
            throw new MyInternalException("Error mapping JSON String to AuthServerResponseDto.", e);
        }
    }
}
