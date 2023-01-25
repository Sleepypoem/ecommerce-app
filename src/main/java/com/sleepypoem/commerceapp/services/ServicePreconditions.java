package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;

public class ServicePreconditions {

    public static <T> T checkEntityNotNull(T entity, String message) throws MyEntityNotFoundException {
        if (entity == null) {
            throw new MyEntityNotFoundException(message);
        }
        return entity;
    }

    public static <T> T checkEntityNotNull(T entity) throws MyEntityNotFoundException {
        if (entity == null) {
            throw new MyEntityNotFoundException("Entity not found");
        }
        return entity;
    }

    public static void checkNotNull(Object object, String message) {
        if (object == null) {
            throw new MyBadRequestException(message);
        }
    }

    public static void checkExpression(boolean expression, String message) throws MyBadRequestException{
        if (!expression) {
            throw new MyBadRequestException(message);
        }
    }
}
