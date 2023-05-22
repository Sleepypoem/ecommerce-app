package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateCheckoutItem implements IValidator<CheckoutItemEntity> {

    @Autowired
    ProductService productService;

    @Override
    public boolean isValid(CheckoutItemEntity element) {
        ProductEntity product = productService.getOneById(element.getId());
        if (product.getStock() <= 0) {
            return false;
        }

        if (product.getStock() - element.getQuantity() < 0) {
            return false;
        }

        return true;
    }
}
