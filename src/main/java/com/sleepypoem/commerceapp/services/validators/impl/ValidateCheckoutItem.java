package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidateCheckoutItem implements IValidator<CheckoutItemEntity> {

    @Autowired
    ProductService productService;

    @Override
    public Map<String, String> isValid(CheckoutItemEntity element) {
        Map<String, String> errors = new HashMap<>();
        ProductEntity product = productService.getOneById(element.getId());
        if (product.getStock() <= 0) {
            errors.put("stock", "The stock cannot be 0 or less.");
        }

        if (product.getStock() - element.getQuantity() < 0) {
            errors.put("stock", "The stock is not enough.");
        }

        return errors;
    }
}
