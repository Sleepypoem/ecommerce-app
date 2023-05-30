package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidateProduct implements IValidator<ProductEntity> {
    @Override
    public Map<String, String> isValid(ProductEntity element) {
        Map<String, String> errors = new HashMap<>();
        if (element.getStock() < 0) {
            errors.put("stock", "The stock cannot be less than 0.");
        }

        if (element.getPrice() < 0) {
            errors.put("price", "The price cannot be less than 0.");
        }

        return errors;
    }
}
