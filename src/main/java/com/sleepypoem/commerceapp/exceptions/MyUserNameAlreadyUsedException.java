package com.sleepypoem.commerceapp.exceptions;

import com.sleepypoem.commerceapp.domain.dto.KeycloakErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyUserNameAlreadyUsedException extends RuntimeException {

    private final KeycloakErrorDto errorDto;

    public MyUserNameAlreadyUsedException(String message, KeycloakErrorDto errorDto) {
        super(message);
        this.errorDto = errorDto;
    }

    public KeycloakErrorDto getErrorDto() {
        return errorDto;
    }
}
