package com.sleepypoem.commerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MyForbiddenException extends RuntimeException {

    public MyForbiddenException(String message) {
        super(message);
    }
}
