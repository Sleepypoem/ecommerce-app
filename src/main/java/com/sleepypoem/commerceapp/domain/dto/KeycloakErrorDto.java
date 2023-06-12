package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sleepypoem.commerceapp.config.beans.ApplicationContextProvider;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KeycloakErrorDto implements Serializable {
    private String error;
    private String errorDescription;

    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    public String toJsonString() {
        return "{" +
                "\"error\":\"" + error + '\"' +
                ", \"errorDescription\":\"" + errorDescription + '\"' +
                '}';
    }

    @Override
    public String toString() {
        return "KeycloakErrorDto{" +
                "error='" + error + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }

    public static KeycloakErrorDto fromJsonString(String jsonString) {
        ObjectMapper mapper = ApplicationContextProvider.applicationContext.getBean(ObjectMapper.class);
        try {
            return mapper.readValue(jsonString, KeycloakErrorDto.class);
        } catch (Exception e) {
            return null;
        }
    }
}
