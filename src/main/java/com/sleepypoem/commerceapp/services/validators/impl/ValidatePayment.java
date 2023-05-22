package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ValidatePayment implements IValidator<PaymentEntity> {

    @Autowired
    @Lazy
    UserService userService;

    @Override
    public boolean isValid(PaymentEntity payment) {
        try {
            userService.getUserById(payment.getUserId());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
