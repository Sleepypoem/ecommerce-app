package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.utils.factories.impl.PaymentMethodFactory;
import com.sleepypoem.commerceapp.utils.factories.impl.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatePaymentMethodTest {

    ValidatePaymentMethod validatePaymentMethod;
    @Mock
    UserService userService;

    PaymentMethodFactory paymentMethodFactory;

    @BeforeEach
    void setUp() {
        validatePaymentMethod = new ValidatePaymentMethod(userService);
        paymentMethodFactory = new PaymentMethodFactory();
    }

    @Test
    @DisplayName("Test validate payment method when no errors")
    void isValid() {
        //arrange
        var paymentMethod = paymentMethodFactory.create();
        when(userService.getOneById(anyString())).thenReturn(new UserFactory().create());
        //act
        var result = validatePaymentMethod.isValid(paymentMethod);
        //assert
        assertThat(result, is(Map.of()));
        verify(userService).getOneById(paymentMethod.getUserId());
    }

    @Test
    @DisplayName("Test validate payment method when user not found")
    void isValidWhenUserNotFound() {
        //arrange
        var paymentMethod = paymentMethodFactory.create();
        when(userService.getOneById(anyString())).thenThrow(new MyUserNotFoundException(""));
        //act
        var result = validatePaymentMethod.isValid(paymentMethod);
        //assert
        assertThat(result, is(Map.of("userId", "User with id: " + paymentMethod.getUserId() + " not found.")));
        verify(userService).getOneById(paymentMethod.getUserId());
    }
}