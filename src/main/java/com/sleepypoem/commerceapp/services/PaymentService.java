package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.PaymentDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutMapper;
import com.sleepypoem.commerceapp.domain.mappers.PaymentMapper;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.repositories.PaymentRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.payment.PaymentChain;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import com.sleepypoem.commerceapp.services.validators.Validator;
import com.sleepypoem.commerceapp.services.validators.impl.ValidatePayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PaymentService extends AbstractService<PaymentDto, PaymentEntity> {

    @Autowired
    PaymentRepository dao;

    @Autowired
    PaymentMapper mapper;

    @Autowired
    PaymentChain paymentChain;

    @Autowired
    CheckoutService checkoutService;

    @Autowired
    IValidator<PaymentEntity> validator;

    @Autowired
    IValidator<PaymentRequestDto> requestValidator;

    @Autowired
    CheckoutMapper checkoutMapper;

    @Override
    protected JpaRepository<PaymentEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected BaseMapper<PaymentEntity, PaymentDto> getMapper() {
        return mapper;
    }

    @Override
    protected IValidator<PaymentEntity> getValidator() {
        return validator;
    }

    public PaymentDto processPayment(PaymentRequestDto paymentRequest) throws Exception {
        Validator.validate(requestValidator, paymentRequest);
        CheckoutEntity checkout = checkoutMapper.convertToEntity(checkoutService.getOneById(paymentRequest.getCheckout().getId()));

        ServicePreconditions.checkExpression(checkout.getStatus() == CheckoutStatus.COMPLETED, "You cannot provide a completed checkout!");
        ServicePreconditions.checkNotNull(checkout.getPaymentMethod(), "You need a payment method to start a payment");
        ServicePreconditions.checkNotNull(checkout.getAddress(), "You need a address to start a payment");
        PaymentDto payment = paymentChain.startChain(paymentRequest);
        Validator.validate(validator, mapper.convertToEntity(payment));
        return super.create(mapper.convertToEntity(payment));
    }
}
