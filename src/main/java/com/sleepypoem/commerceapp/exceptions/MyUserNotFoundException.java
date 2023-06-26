package com.sleepypoem.commerceapp.exceptions;

import com.sleepypoem.commerceapp.domain.dto.KeycloakErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyUserNotFoundException extends RuntimeException {

    private final KeycloakErrorDto errorDto;

    public MyUserNotFoundException(String message) {
        super(message);
        this.errorDto = null;
    }

    public MyUserNotFoundException(String message, KeycloakErrorDto errorDto) {
        super(message);
        this.errorDto = errorDto;
    }

    public KeycloakErrorDto getErrorDto() {
        return errorDto;
    }
}
