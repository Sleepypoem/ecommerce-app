package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.config.payment.StripeFacade;
import com.sleepypoem.commerceapp.config.payment.StripeFacadeImpl;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.exceptions.MyStripeException;
import com.sleepypoem.commerceapp.repositories.PaymentMethodRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.abstracts.HaveUser;
import com.sleepypoem.commerceapp.services.validators.impl.ValidatePaymentMethod;
import com.stripe.model.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validable(ValidatePaymentMethod.class)
@Slf4j
public class PaymentMethodService extends AbstractService<PaymentMethodEntity, Long> implements HaveUser<PaymentMethodEntity> {

    private final PaymentMethodRepository dao;

    private final StripeFacade stripeFacade;

    public PaymentMethodService(PaymentMethodRepository dao, StripeFacadeImpl stripeFacade) {
        this.dao = dao;
        this.stripeFacade = stripeFacade;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentMethodEntity createCard(String cardToken, String userId) {
        PaymentMethodEntity entity = new PaymentMethodEntity();
        PaymentMethod paymentMethod;
        String customerId;
        log.info("Creating card for user: {}", userId);
        try {
            paymentMethod = stripeFacade.createPaymentMethod(cardToken);
            customerId = stripeFacade.createCustomer(userId);
            stripeFacade.attachPaymentMethod(customerId, paymentMethod.getId());
        } catch (Exception e) {
            throw new MyStripeException(e.getMessage());
        }
        entity.setUserId(userId);
        entity.setStripeUserId(customerId);
        entity.setPaymentId(paymentMethod.getId());
        entity.setPaymentType(paymentMethod.getType());
        entity.setBrand(paymentMethod.getCard().getBrand());
        entity.setLast4(paymentMethod.getCard().getLast4());
        entity.setExpMonth(String.valueOf(paymentMethod.getCard().getExpMonth()));
        entity.setExpYear(String.valueOf(paymentMethod.getCard().getExpYear()));
        log.info("Card created: {}", entity);

        return super.create(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentMethodEntity updateCard(Long id, String cardToken) {
        PaymentMethodEntity entity = getOneById(id);
        PaymentMethod paymentMethod;
        log.info("Updating card...");
        try {
            paymentMethod = stripeFacade.updatePaymentMethod(entity.getPaymentId(), cardToken);
        } catch (Exception e) {
            throw new MyStripeException(e.getMessage());
        }
        entity.setBrand(paymentMethod.getCard().getBrand());
        entity.setLast4(paymentMethod.getCard().getLast4());
        entity.setExpMonth(String.valueOf(paymentMethod.getCard().getExpMonth()));
        entity.setExpYear(String.valueOf(paymentMethod.getCard().getExpYear()));
        log.info("Card updated!");

        return super.update(id, entity);
    }

    @Override
    protected JpaRepository<PaymentMethodEntity, Long> getDao() {
        return dao;
    }

    @Override
    public Page<PaymentMethodEntity> getAllPaginatedAndSortedByUserId(String userId, int page, int size, String sortBy, String sortOrder) {
        return dao.findByUserId(userId, PageRequest.of(page, size, createSort(sortBy, sortOrder)));
    }
}
