package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.config.payment.StripeFacadeImpl;
import com.sleepypoem.commerceapp.domain.entities.CardEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.exceptions.MyStripeException;
import com.sleepypoem.commerceapp.repositories.CardRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.abstracts.HaveUser;
import com.stripe.model.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class CardService extends AbstractService<CardEntity, Long> implements HaveUser<CardEntity> {

    private final CardRepository cardRepository;

    private final StripeFacadeImpl stripeFacade;

    private final StripeUserService stripeUserService;

    public CardService(CardRepository cardRepository, StripeFacadeImpl stripeFacade, StripeUserService stripeUserService) {
        this.cardRepository = cardRepository;
        this.stripeFacade = stripeFacade;
        this.stripeUserService = stripeUserService;
    }
    @Override
    protected JpaRepository<CardEntity, Long> getDao() {
        return cardRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public PaymentMethodEntity createCard(String cardToken, String userId) {
        CardEntity entity = new CardEntity();
        PaymentMethod paymentMethod;
        stripeUserService.create(userId);
        try {
            paymentMethod = stripeFacade.createPaymentMethod(cardToken);
            stripeFacade.attachPaymentMethod(paymentMethod.getId(), userId);
            entity.setUserId(userId);
            entity.setPaymentId(paymentMethod.getId());
            entity.setPaymentType(paymentMethod.getType());
            entity.setBrand(paymentMethod.getCard().getBrand());
            entity.setLast4(paymentMethod.getCard().getLast4());
            entity.setExpMonth(String.valueOf(paymentMethod.getCard().getExpMonth()));
            entity.setExpYear(String.valueOf(paymentMethod.getCard().getExpYear()));

        } catch (Exception e) {
            throw new MyStripeException(e.getMessage());
        }

        return super.create(entity);
    }

    @Override
    public Page<CardEntity> getAllPaginatedAndSortedByUserId(String userId, int page, int size, String sortBy, String sortOrder) {
        return cardRepository.findByUserId(userId, PageRequest.of(page, size, createSort(sortBy, sortOrder)));
    }
}
