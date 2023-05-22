package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ValidateCheckout implements IValidator<CheckoutEntity> {

    @Autowired
    @Lazy
    UserService userService;

    @Autowired
    @Lazy
    ProductService productService;

    @Override
    public boolean isValid(CheckoutEntity checkout) {
        try {
            userService.getUserById(checkout.getUserId());
        } catch (Exception e) {
            return false;
        }

        for (CheckoutItemEntity item : checkout.getItems()) {

            ProductEntity product = productService.getOneById(item.getProduct().getId());

            if ((product.getStock() - item.getQuantity()) < 0) {
                return false;
            }
        }

        for (CheckoutItemEntity item : checkout.getItems()) {
            if (item.getQuantity() <= 0) {
                return false;
            }
        }
        return true;
    }
}
