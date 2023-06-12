package com.sleepypoem.commerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MyInternalException extends RuntimeException {

    public MyInternalException(String message) {
        super(message);
    }

    public MyInternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
