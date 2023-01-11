package com.sleepypoem.commerceapp.exceptions;

public class MyUnsupportedPaymentMethodException extends Exception {

    public MyUnsupportedPaymentMethodException(String message) {
        super(message);
    }

    public MyUnsupportedPaymentMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public MyUnsupportedPaymentMethodException(Throwable cause) {
        super(cause);
    }
}
