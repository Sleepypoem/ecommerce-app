package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidatePaymentMethod implements IValidator<PaymentMethodEntity> {

    private final UserService userService;

    public ValidatePaymentMethod(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Map<String, String> isValid(PaymentMethodEntity paymentMethod) {
        Map<String, String> errors = new HashMap<>();
        String userId = paymentMethod.getUserId();
        try {
            userService.getOneById(userId);
        } catch (MyUserNotFoundException e) {
            errors.put("userId", "User with id: " + userId + " not found.");
        }
        return errors;
    }
}
