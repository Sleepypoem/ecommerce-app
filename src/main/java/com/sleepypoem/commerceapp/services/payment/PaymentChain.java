package com.sleepypoem.commerceapp.services.payment;

import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.exceptions.MyUnsupportedPaymentMethodException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PaymentChain {

    List<IHandler> handlers;

    public PaymentChain() {
        handlers = new ArrayList<>();
        this.handlers.add(new PaypalHandler());
        this.handlers.add(new CreditCardHandler());
    }

    public PaymentEntity startChain(PaymentRequestDto paymentRequest) throws MyUnsupportedPaymentMethodException {
        for (IHandler handler : handlers) {
            handler.setPaymentMethod(paymentRequest);
            if (handler.canHandle()) {
                return handler.handle();
            }
        }

        throw new MyUnsupportedPaymentMethodException("Unsupported payment method");
    }
}
