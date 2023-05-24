package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.repositories.PaymentMethodRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.impl.ValidatePaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Validable(ValidatePaymentMethod.class)
public class PaymentMethodService extends AbstractService<PaymentMethodEntity> {

    private final PaymentMethodRepository dao;

    public PaymentMethodService(PaymentMethodRepository dao) {
        this.dao = dao;
    }

    @Override
    protected JpaRepository<PaymentMethodEntity, Long> getDao() {
        return dao;
    }

    public List<PaymentMethodEntity> findByUserId(String userId) {
        return dao.findByUserId(userId);
    }


}
