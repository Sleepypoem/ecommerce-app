package com.sleepypoem.commerceapp.exceptions;

import com.sleepypoem.commerceapp.domain.dto.KeycloakErrorDto;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Keycloak server error")
public class MyAuthServerException extends RuntimeException {
    private final KeycloakErrorDto errorDto;

    public MyAuthServerException(String message, KeycloakErrorDto errorDto) {
        super(message);
        this.errorDto = errorDto;
    }

    public KeycloakErrorDto getErrorDto() {
        return errorDto;
    }
}
