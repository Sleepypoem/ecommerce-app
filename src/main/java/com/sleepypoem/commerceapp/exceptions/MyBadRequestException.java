package com.sleepypoem.commerceapp.exceptions;

import com.sleepypoem.commerceapp.domain.dto.KeycloakErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class MyBadRequestException extends RuntimeException {

    private KeycloakErrorDto errorDto;

    public MyBadRequestException(final String message, KeycloakErrorDto errorDto) {
        super(message);
        this.errorDto = errorDto;
    }

    public MyBadRequestException(final String message) {
        super(message);
    }

    public MyBadRequestException(final Throwable cause, KeycloakErrorDto errorDto) {
        super(cause);
        this.errorDto = errorDto;
    }

    public KeycloakErrorDto getErrorDto() {
        return errorDto;
    }

}
