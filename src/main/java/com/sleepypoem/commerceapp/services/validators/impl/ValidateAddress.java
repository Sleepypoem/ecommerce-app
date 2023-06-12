package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ValidateAddress implements IValidator<AddressEntity> {
    @Override
    public Map<String, String> isValid(AddressEntity address) {
        UserService userService = getApplicationContext().getBean(UserService.class);
        Map<String, String> errors = new HashMap<>();
        String userId = address.getUserId();
        try {
            userService.getUserById(userId);
        } catch (MyUserNotFoundException e) {
            errors.put("userId", "User with id: " + userId + " not found.");
        }
        return errors;
    }
}
