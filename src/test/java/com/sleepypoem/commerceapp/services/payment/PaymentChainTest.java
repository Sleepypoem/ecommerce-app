package com.sleepypoem.commerceapp.services.payment;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.exceptions.MyUnsupportedPaymentMethodException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentChainTest {

    private PaymentChain chain;

    private PaymentRequestDto paymentRequest;

    @BeforeEach
    void init() {
        CheckoutDto checkout = CheckoutDto.builder().paymentMethod(PaymentMethodEntity.builder().paymentType("WRONG").build()).build();
        paymentRequest = PaymentRequestDto
                .builder()
                .checkout(checkout)
                .build();
        chain = new PaymentChain();
    }

    @Test
    void testExceptionIsThrownWhenNotRegisteredHandler() {
        assertThrows(MyUnsupportedPaymentMethodException.class, () -> {
            chain.startChain(paymentRequest);
        });
    }

}