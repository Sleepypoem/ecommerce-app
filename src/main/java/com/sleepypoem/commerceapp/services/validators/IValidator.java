package com.sleepypoem.commerceapp.services.validators;

public interface IValidator<T> {

    /**
     * It takes care of the validation logic.
     *
     * @param element The element that is going to be validated.
     * @return True if the object is valid, false otherwise.
     */
    boolean isValid(T element) throws Exception;
}
