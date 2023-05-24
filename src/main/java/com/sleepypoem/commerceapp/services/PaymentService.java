package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.repositories.PaymentRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.payment.PaymentChain;
import com.sleepypoem.commerceapp.services.validators.impl.ValidatePayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Validable(ValidatePayment.class)
public class PaymentService extends AbstractService<PaymentEntity> {

    private final PaymentRepository dao;

    private final PaymentChain paymentChain;

    public PaymentService(PaymentRepository dao, PaymentChain paymentChain) {
        this.dao = dao;
        this.paymentChain = paymentChain;
    }

    @Override
    protected JpaRepository<PaymentEntity, Long> getDao() {
        return dao;
    }

    public PaymentEntity processPayment(PaymentRequestDto paymentRequest) throws Exception {
        ServicePreconditions.checkEntityNotNull(paymentRequest.getCheckout(), "You need to start a checkout to start a payment");
        ServicePreconditions.checkEntityNotNull(paymentRequest.getCheckout().getPaymentMethod(), "You need a payment method to start a payment");
        ServicePreconditions.checkEntityNotNull(paymentRequest.getCheckout().getAddress(), "You need a address to start a payment");
        PaymentEntity payment = paymentChain.startChain(paymentRequest);
        log.info(payment.toString());
        return super.create(payment);
    }
}
