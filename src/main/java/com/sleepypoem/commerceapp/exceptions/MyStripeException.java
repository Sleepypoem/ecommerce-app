package com.sleepypoem.commerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MyStripeException extends RuntimeException {

    public MyStripeException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "MyStripeException{" +
                "message='" + getMessage() + '\'' +
                "}";
    }
}
