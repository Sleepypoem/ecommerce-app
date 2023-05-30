package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidateCheckoutItem implements IValidator<CheckoutItemEntity> {

    @Override
    public Map<String, String> isValid(CheckoutItemEntity element) {
        Map<String, String> errors = new HashMap<>();
        if (element.getQuantity() < 0) {
            errors.put("quantity", "The quantity cannot be less than 0.");
        }

        return errors;
    }
}
