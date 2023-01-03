package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;

public class ServicePreconditions {

    public static <T> T checkEntityNotNull(T entity) throws MyEntityNotFoundException {
        if (entity == null) {
            throw new MyEntityNotFoundException("Entity not found");
        }
        return entity;
    }

    public static void checkExpression(boolean expression, String message) {
        if (!expression) {
            throw new MyBadRequestException(message);
        }
    }
}
