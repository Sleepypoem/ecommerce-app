package com.sleepypoem.commerceapp.services.validators;

import com.sleepypoem.commerceapp.exceptions.MyValidationException;

public class Validator<T> {

    private final IValidator<T> iValidator;

    private final T element;

    public Validator(IValidator<T> validator, T element) {
        this.iValidator = validator;
        this.element = element;
    }

    public void validate() {
        boolean isValid = iValidator.isValid(element);
        if (!isValid) {
            throw new MyValidationException("Element is not valid");
        }
    }
}
