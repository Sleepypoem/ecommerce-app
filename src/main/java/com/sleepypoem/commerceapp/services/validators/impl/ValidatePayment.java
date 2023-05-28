package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidatePayment implements IValidator<PaymentEntity> {
    @Override
    public Map<String, String> isValid(PaymentEntity payment) {
        UserService userService = getApplicationContext().getBean(UserService.class);

        Map<String, String> errors = new HashMap<>();
        try {
            userService.getUserById(payment.getUserId());
        } catch (Exception e) {
            errors.put("userId", "The user does not exist.");
        }
        return errors;
    }
}
