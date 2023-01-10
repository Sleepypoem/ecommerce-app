package com.sleepypoem.commerceapp.services.helpers;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.services.AddressService;
import com.sleepypoem.commerceapp.services.CheckoutService;
import com.sleepypoem.commerceapp.services.PaymentMethodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class UserResourceBinder {

    @Autowired
    private CheckoutService checkoutService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    public UserDto attachCheckout(UserDto user) {
        CheckoutEntity checkout = checkoutService.getByUserId(user.getId());
        user.setCheckouts(checkout);
        return user;
    }

    public UserDto attachAddresses(UserDto user) {
        List<AddressEntity> addresses = addressService.findByUserId(user.getId());
        user.setAddresses(addresses);
        return user;
    }

    public UserDto attachPaymentMethods(UserDto user) {
        List<PaymentMethodEntity> paymentMethods = paymentMethodService.findByUserId(user.getId());
        user.setPaymentMethods(paymentMethods);
        return user;
    }
}
