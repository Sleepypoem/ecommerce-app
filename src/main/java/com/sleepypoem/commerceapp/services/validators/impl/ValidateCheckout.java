package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class ValidateCheckout implements IValidator<CheckoutEntity> {

    @Override
    public Map<String, String> isValid(CheckoutEntity checkout) {
        UserService userService = getApplicationContext().getBean(UserService.class);
        ProductService productService = getApplicationContext().getBean(ProductService.class);
        Map<String, String> errors = new HashMap<>();
        String userId = checkout.getUserId();
        try {
            userService.getUserById(userId);
        } catch (MyUserNotFoundException e) {
            errors.put("userId", "User with id: " + userId + " not found.");
        }

        if (Objects.equals(checkout.getStatus(), CheckoutStatus.CANCELED) || Objects.equals(checkout.getStatus(), CheckoutStatus.COMPLETED)) {
            errors.put("status", "Must provide a pending checkout. Provided status: " + checkout.getStatus() + ".");
        }

        for (CheckoutItemEntity item : checkout.getItems()) {

            ProductEntity product = productService.getOneById(item.getProduct().getId());

            if ((product.getStock() - item.getQuantity()) < 0) {
                errors.put("stock", "The stock is not enough. Current stock: " + product.getStock() + ".");
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
