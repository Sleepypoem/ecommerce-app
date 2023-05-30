package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.repositories.PaymentMethodRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.abstracts.HaveUser;
import com.sleepypoem.commerceapp.services.validators.impl.ValidatePaymentMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Validable(ValidatePaymentMethod.class)
public class PaymentMethodService extends AbstractService<PaymentMethodEntity> implements HaveUser<PaymentMethodEntity> {

    private final PaymentMethodRepository dao;

    public PaymentMethodService(PaymentMethodRepository dao) {
        this.dao = dao;
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
