package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.config.payment.StripeFacade;
import com.sleepypoem.commerceapp.domain.dto.PaymentIntentDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.domain.enums.Currency;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.repositories.PaymentRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.abstracts.HaveUser;
import com.sleepypoem.commerceapp.services.validators.impl.ValidatePayment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
@Validable(ValidatePayment.class)
public class PaymentService extends AbstractService<PaymentEntity, Long> implements HaveUser<PaymentEntity> {

    private final PaymentRepository dao;
    private final StripeFacade stripeFacade;
    private final CheckoutService checkoutService;

    public PaymentService(PaymentRepository dao, StripeFacade stripeFacade, CheckoutService checkoutService) {
        this.dao = dao;
        this.stripeFacade = stripeFacade;
        this.checkoutService = checkoutService;
    }

    @Override
    protected JpaRepository<PaymentEntity, Long> getDao() {
        return dao;
    }

    public PaymentEntity startPayment(PaymentRequestDto paymentRequest) {
        CheckoutEntity checkout = checkoutService.getOneById(paymentRequest.getCheckout().getId());
        validateCheckoutBeforeProcessing(checkout);
        PaymentEntity payment = new PaymentEntity();
        payment.setUserId(checkout.getUserId());
        payment.setCheckout(checkout);
        payment.setCurrency(paymentRequest.getCurrency() == null ? Currency.USD : paymentRequest.getCurrency());
        payment.setStatus(PaymentStatus.PROCESSING);
        return super.create(payment);
    }

    public PaymentEntity confirmPayment(Long paymentId) {
        PaymentEntity payment = getOneById(paymentId);
        ServicePreconditions.checkExpression(payment.getStatus().equals(PaymentStatus.PROCESSING), "Payment is not processing");
        PaymentIntentDto paymentIntentDto;
        try {
            paymentIntentDto = stripeFacade.createAndConfirmPaymentIntent(
                    payment.getCheckout().getPaymentMethod().getStripeUserId(),
                    payment.getCheckout().getPaymentMethod().getPaymentId(),
                    payment.getCheckout().getTotal().multiply(BigDecimal.valueOf(100)).intValue(),
                    payment.getCurrency().toString()
            );
        } catch (Exception e) {
            log.error("Error confirming payment: id={}", paymentId, e);
            payment.setStatus(PaymentStatus.FAILED);
            payment.setPaymentProviderMessage(e.getMessage());
            return super.update(paymentId, payment);
        }
        payment.setStatus(PaymentStatus.SUCCESS);
        checkoutService.setStatusToCompleted(payment.getCheckout().getId());
        payment.setPaymentProviderMessage("Status: " + paymentIntentDto.getStatus());
        return super.update(paymentId, payment);
    }

    public PaymentEntity cancelPayment(Long paymentId) {
        PaymentEntity payment = getOneById(paymentId);
        ServicePreconditions.checkExpression(payment.getStatus().equals(PaymentStatus.PROCESSING), "Payment is not processing");
        checkoutService.setStatusToCanceled(payment.getCheckout().getId());
        payment.setStatus(PaymentStatus.CANCELLED);
        payment.setPaymentProviderMessage("Status: cancelled");
        return super.update(paymentId, payment);
    }

    @Override
    public Page<PaymentEntity> getAllPaginatedAndSortedByUserId(String userId, int page, int size, String sortBy, String sortOrder) {
        return dao.findByUserId(userId, PageRequest.of(page, size, createSort(sortBy, sortOrder)));
    }

    private void validateCheckoutBeforeProcessing(CheckoutEntity checkout) {
        ServicePreconditions.checkExpression(checkout.getStatus().equals(CheckoutStatus.PENDING), "Checkout is not pending");
        ServicePreconditions.checkEntityNotNull(checkout.getPaymentMethod(), "Payment method not set");
        ServicePreconditions.checkEntityNotNull(checkout.getAddress(), "Address not set");
    }
}
