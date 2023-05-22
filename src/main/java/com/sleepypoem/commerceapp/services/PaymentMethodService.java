package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.repositories.PaymentMethodRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService extends AbstractService<PaymentMethodEntity> {

    @Autowired
    private IValidator<PaymentMethodEntity> validatePaymentMethod;

    @Autowired
    PaymentMethodRepository dao;

    @Override
    protected JpaRepository<PaymentMethodEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected IValidator<PaymentMethodEntity> getValidator() {
        return validatePaymentMethod;
    }

    public List<PaymentMethodEntity> findByUserId(String userId) {
        return dao.findByUserId(userId);
    }


}
