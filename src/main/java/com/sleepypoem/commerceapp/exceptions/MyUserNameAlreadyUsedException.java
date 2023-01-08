package com.sleepypoem.commerceapp.exceptions;

public class MyUserNameAlreadyUsedException extends Exception{
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
