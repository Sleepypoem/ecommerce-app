package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ValidatePaymentMethod implements IValidator<PaymentMethodEntity> {


    @Autowired
    @Lazy
    UserService userService;

    @Override
    public boolean isValid(PaymentMethodEntity paymentMethod) {
        try {
            userService.getUserById(paymentMethod.getUserId());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
