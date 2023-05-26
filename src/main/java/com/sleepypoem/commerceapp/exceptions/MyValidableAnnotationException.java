package com.sleepypoem.commerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class MyValidableAnnotationException extends RuntimeException {
    public MyValidableAnnotationException(String message) {
        super(message);
    }

    public MyValidableAnnotationException() {
    }

    public MyValidableAnnotationException(String message, Throwable cause) {
        super(message, cause);
    }
}
