package com.sleepypoem.commerceapp.services.validators;

import java.util.Map;

public interface IValidator<T> {
    /**
     * It takes care of the validation logic.
     *
     * @param element The element that is going to be validated.
     * @return A HashMap with the errors and the error messages.
     */
    Map<String, String> isValid(T element);
}
