package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.config.payment.StripeFacadeImpl;
import com.sleepypoem.commerceapp.domain.entities.StripeUserEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyStripeException;
import com.sleepypoem.commerceapp.repositories.StripeUserRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.stereotype.Service;

@Service
public class StripeUserService extends AbstractService<StripeUserEntity, String> {

    private final StripeFacadeImpl stripeFacade;

    private final StripeUserRepository stripeUserRepository;

    public StripeUserService(StripeFacadeImpl stripeFacade, StripeUserRepository stripeUserRepository) {
        this.stripeFacade = stripeFacade;
        this.stripeUserRepository = stripeUserRepository;
    }
    @Override
    protected StripeUserRepository getDao() {
        return stripeUserRepository;
    }

    public StripeUserEntity create(String userId) {
        String stripeId;
        try {
            stripeId = stripeFacade.createCustomer(userId);
        } catch (Exception e) {
            throw new MyStripeException(e.getMessage());
        }
        StripeUserEntity entity = new StripeUserEntity();
        entity.setId(stripeId);
        entity.setUserId(userId);
        return super.create(entity);
    }

    public StripeUserEntity getStripeUserByUserId(String userId) {
        return stripeUserRepository.findByUserId(userId).orElseThrow(() -> new MyEntityNotFoundException("Stripe user not found"));
    }
}
