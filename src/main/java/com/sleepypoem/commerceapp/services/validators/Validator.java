package com.sleepypoem.commerceapp.services.validators;

import com.sleepypoem.commerceapp.exceptions.MyValidationException;

public class Validator {

    /**
     * It validates an object based on the validator passed as parameter.
     *
     * @param validator A validator that contains the validation logic.
     * @param element   The element that is going to be validated.
     * @param <T>       The element that is going to be validated.
     * @return True if is valid, false otherwise.
     */
    public static <T> void validate(IValidator<T> validator, T element) throws MyValidationException {
        boolean isValid = validator.isValid(element);
        if (!isValid) {
            throw new MyValidationException("Element is not valid");
        }
    }
}
