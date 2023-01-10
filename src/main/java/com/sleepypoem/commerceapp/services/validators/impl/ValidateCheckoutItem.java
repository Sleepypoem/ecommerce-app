package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ValidateCheckoutItem implements IValidator<CheckoutItemEntity> {

    @Autowired
    ProductService productService;

    @Override
    public boolean isValid(CheckoutItemEntity element) throws Exception {
        Optional<ProductDto> optionalProductDto = productService.getOneById(element.getProduct().getId());
        ProductDto product;
        if (optionalProductDto.isEmpty()) {
            return false;
        } else {
            product = optionalProductDto.get();
        }

        if (product.getStock() <= 0) {
            return false;
        }

        if (product.getStock() - element.getQuantity() < 0) {
            return false;
        }

        return true;
    }
}
