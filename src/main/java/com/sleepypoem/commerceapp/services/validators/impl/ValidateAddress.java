package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ValidateAddress implements IValidator<AddressEntity> {

    @Autowired
    @Lazy
    UserService userService;

    @Override
    public boolean isValid(AddressEntity address) {
        try {
            userService.getUserById(address.getUserId());
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}
