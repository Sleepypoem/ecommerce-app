package com.sleepypoem.commerceapp.services.validators;

import com.sleepypoem.commerceapp.exceptions.MyValidationException;

import java.util.Map;

public class DetailedValidator<T> {

    private final IValidator<T> iValidator;

    private final T element;

    public DetailedValidator(IValidator<T> validator, T element) {
        this.iValidator = validator;
        this.element = element;
    }

    public void validate() {
        Map<String, String> errors = iValidator.isValid(element);
        if (!errors.isEmpty()) {
            throw new MyValidationException(errors);
        }
    }
}
