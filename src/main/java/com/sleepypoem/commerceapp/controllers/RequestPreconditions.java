package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;

public class RequestPreconditions {

    public static <T> T checkNotNull(final T reference) throws MyBadRequestException {
        return checkNotNull(reference, null);
    }

    public static <T> T checkNotNull(final T reference, final String message) throws MyBadRequestException {
        if (reference == null) {
            throw new MyBadRequestException(message);
        }
        return reference;
    }

    public static <T> T checkRequestElementNotNull(final T reference, final String message) throws MyBadRequestException {
        if (reference == null) {
            throw new MyBadRequestException(message);
        }
        return reference;
    }

    public static <T> T checkRequestElementNotNull(final T reference) throws MyBadRequestException {
        return checkRequestElementNotNull(reference, null);
    }

    public static void checkRequestState(final boolean expression, final String message) throws MyBadRequestException {
        if (!expression) {
            throw new MyBadRequestException(message);
        }
    }

    public static void checkRequestState(final boolean expression) throws MyBadRequestException {
        checkRequestState(expression, null);
    }
}
