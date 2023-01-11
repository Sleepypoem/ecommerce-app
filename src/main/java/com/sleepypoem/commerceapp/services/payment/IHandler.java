package com.sleepypoem.commerceapp.services.payment;

import com.sleepypoem.commerceapp.domain.dto.PaymentDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;

public interface IHandler {

    void setPaymentMethod(PaymentRequestDto paymentRequestDto);

    PaymentDto handle();

    boolean canHandle();
}
