package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.repositories.PaymentRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.payment.PaymentChain;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentService extends AbstractService<PaymentEntity> {

    @Autowired
    PaymentRepository dao;

    @Autowired
    PaymentChain paymentChain;

    @Autowired
    IValidator<PaymentEntity> validator;

    @Override
    protected JpaRepository<PaymentEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected IValidator<PaymentEntity> getValidator() {
        return validator;
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
