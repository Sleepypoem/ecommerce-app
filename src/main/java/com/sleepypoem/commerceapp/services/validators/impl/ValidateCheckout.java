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

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidateCheckout implements IValidator<CheckoutEntity> {

    @Autowired
    @Lazy
    UserService userService;

    @Autowired
    @Lazy
    ProductService productService;

    @Override
    public Map<String, String> isValid(CheckoutEntity checkout) {
        Map<String, String> errors = new HashMap<>();
        try {
            userService.getUserById(checkout.getUserId());
        } catch (Exception e) {
            errors.put("userId", "The user does not exist.");
        }

        for (CheckoutItemEntity item : checkout.getItems()) {

            ProductEntity product = productService.getOneById(item.getProduct().getId());

            if ((product.getStock() - item.getQuantity()) < 0) {
                errors.put("stock", "The stock is not enough.");
            }
        }

        for (CheckoutItemEntity item : checkout.getItems()) {
            if (item.getQuantity() <= 0) {
                errors.put("quantity", "The quantity must be greater than 0.");
            }
        }
        return errors;
    }
}
