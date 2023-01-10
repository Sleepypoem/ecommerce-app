package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateCheckout implements IValidator<CheckoutEntity> {

    @Autowired
    @Lazy
    UserService userService;

    @Autowired
    @Lazy
    ProductService productService;

    @Override
    public boolean isValid(CheckoutEntity checkout) throws Exception {
        if (userService.getUserById(checkout.getUserId()) == null) {
            return false;
        }

        for (CheckoutItemEntity item : checkout.getItems()) {
            Optional<ProductDto> optProduct = productService.getOneById(item.getProduct().getId());
            if (optProduct.isEmpty()) {
                return false;
            }

            ProductDto product = optProduct.get();

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
