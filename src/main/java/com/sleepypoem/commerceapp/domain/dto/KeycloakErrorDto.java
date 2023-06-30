package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.config.beans.GsonProvider;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class KeycloakErrorDto implements Serializable {
    private String error;
    private String error_description;

    @Serial
    private static final long serialVersionUID = 2405172041950251807L;

    public String toJsonString() {
        return GsonProvider.getGson().toJson(this);
    }

    @Override
    public String toString() {
        return "KeycloakErrorDto{" +
                "error='" + error + '\'' +
                ", errorDescription='" + error_description + '\'' +
                '}';
    }

    public static KeycloakErrorDto fromJsonString(String jsonString) {
        return GsonProvider.getGson().fromJson(jsonString, KeycloakErrorDto.class);
    }
}
