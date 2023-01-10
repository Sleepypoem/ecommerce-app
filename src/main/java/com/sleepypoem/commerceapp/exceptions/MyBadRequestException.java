package com.sleepypoem.commerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public final class MyBadRequestException extends RuntimeException {

    public MyBadRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MyBadRequestException(final String message) {
        super(message);
    }

    public MyBadRequestException(final Throwable cause) {
        super(cause);
    }

}
