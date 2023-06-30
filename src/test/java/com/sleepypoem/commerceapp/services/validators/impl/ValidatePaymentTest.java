package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.utils.factories.impl.PaymentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidatePaymentTest {

    ValidatePayment validatePayment;

    @Mock
    UserService userService;

    PaymentFactory paymentFactory;

    @BeforeEach
    void setUp() {
        validatePayment = new ValidatePayment(userService);
        paymentFactory = new PaymentFactory();
    }

    @Test
    @DisplayName("Test validatePayment when no errors")
    void testValidatePaymentWhenNoErrors() {
        //arrange
        var payment = paymentFactory.create();
        //act
        var result = validatePayment.isValid(payment);
        //assert
        assertEquals(result, Map.of());
    }

    @Test
    @DisplayName("Test validatePayment when userId is not found")
    void testValidatePaymentWhenUserIdIsNotFound() {
        //arrange
        var payment = paymentFactory.create();
        when(userService.getOneById(anyString())).thenThrow(MyUserNotFoundException.class);
        //act
        var result = validatePayment.isValid(payment);
        //assert
        assertEquals(result, Map.of("userId", "User with id: " + payment.getUserId() + " not found."));
        verify(userService).getOneById(payment.getUserId());
    }

}