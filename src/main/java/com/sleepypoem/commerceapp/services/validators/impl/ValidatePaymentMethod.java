package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidatePaymentMethod implements IValidator<PaymentMethodEntity> {

    @Override
    public Map<String, String> isValid(PaymentMethodEntity paymentMethod) {
        UserService userService = getApplicationContext().getBean(UserService.class);
        Map<String, String> errors = new HashMap<>();
        try {
            userService.getUserById(paymentMethod.getUserId());
        } catch (Exception e) {
            errors.put("userId", "The user does not exist.");
        }
        return errors;
    }
}
