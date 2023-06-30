package com.sleepypoem.commerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public final class MyEntityNotFoundException extends RuntimeException {

    public MyEntityNotFoundException(final String message) {
        super(message);
    }

}
