package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ValidateAddress implements IValidator<AddressEntity> {
    @Override
    public Map<String, String> isValid(AddressEntity address) {
        UserService userService = getApplicationContext().getBean(UserService.class);
        Map<String, String> errors = new HashMap<>();
        try {
            userService.getUserById(address.getUserId());
        } catch (Exception e) {
            errors.put("userId", "The user " + address.getUserId() + " does not exist");
            errors.put("other", e.getMessage());
        }
        return errors;
    }
}
