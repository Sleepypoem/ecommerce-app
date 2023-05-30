package com.sleepypoem.commerceapp.services.payment;

import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;

public interface IHandler {

    void setPaymentMethod(PaymentRequestDto paymentRequestDto);

    PaymentEntity handle();

    boolean canHandle();
}
