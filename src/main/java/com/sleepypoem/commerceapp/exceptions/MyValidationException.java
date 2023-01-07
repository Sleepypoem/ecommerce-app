package com.sleepypoem.commerceapp.exceptions;

import java.util.Map;

public class MyValidationException extends Exception{

    public MyValidationException(String message) {
        super(message);
    }

    public MyValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyValidationException(Throwable cause) {
        super(cause);
    }

}
