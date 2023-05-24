package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.stereotype.Component;

@Component
public class ValidateProduct implements IValidator<ProductEntity> {
    @Override
    public boolean isValid(ProductEntity element) {
        if (element.getStock() < 0) {
            return false;
        }

        return element.getPrice() > 0;
    }
}
