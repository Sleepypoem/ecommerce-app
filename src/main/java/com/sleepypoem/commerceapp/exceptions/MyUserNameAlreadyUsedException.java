package com.sleepypoem.commerceapp.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyUserNameAlreadyUsedException extends Exception {
    public MyUserNameAlreadyUsedException(String message) {
        super(message);
    }

    public MyUserNameAlreadyUsedException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyUserNameAlreadyUsedException(Throwable cause) {
        super(cause);
    }
}
