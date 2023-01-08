package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyResourceNotFoundException;

public class RequestPreconditions {

    public static <T> T checkNotNull(final T reference) {
        return checkNotNull(reference, null);
    }

    public static <T> T checkNotNull(final T reference, final String message) {
        if (reference == null) {
            throw new MyResourceNotFoundException(message);
        }
        return reference;
    }

    public static <T> T checkRequestElementNotNull(final T reference, final String message) {
        if (reference == null) {
            throw new MyBadRequestException(message);
        }
        return reference;
    }

    public static <T> T checkRequestElementNotNull(final T reference) {
        return checkRequestElementNotNull(reference, null);
    }

    public static void checkRequestState(final boolean expression, final String message) {
        if (!expression) {
            throw new MyBadRequestException(message);
        }
    }

    public static void checkRequestState(final boolean expression) {
        checkRequestState(expression, null);
    }
}
