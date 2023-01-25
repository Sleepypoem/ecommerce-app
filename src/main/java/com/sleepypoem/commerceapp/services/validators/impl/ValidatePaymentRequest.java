package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ValidatePaymentRequest implements IValidator<PaymentRequestDto> {

    @Autowired
    @Lazy
    UserService userService;

    @Override
    public boolean isValid(PaymentRequestDto paymentRequestDto) throws MyValidationException {
        if(paymentRequestDto.getUser() == null) {
            return false;
        }

        try{
            userService.getUserById(paymentRequestDto.getUser().getId());
        }catch(Exception e) {
            throw new MyValidationException("User not found");
        }

        return paymentRequestDto.getCheckout() != null;
    }
}
