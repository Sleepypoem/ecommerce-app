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
    public boolean isValid(PaymentMethodEntity paymentMethod) throws Exception {
        if (userService.getUserById(paymentMethod.getUserId()) == null) {
            return false;
        }
        return true;
    }
}
