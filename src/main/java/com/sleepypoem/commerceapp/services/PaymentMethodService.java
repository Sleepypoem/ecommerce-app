package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.PaymentMethodMapper;
import com.sleepypoem.commerceapp.repositories.PaymentMethodRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService extends AbstractService<PaymentMethodDto, PaymentMethodEntity> {

    final static IValidator<PaymentMethodEntity> VALIDATE_PAYMENT_METHOD = paymentMethod -> {
        return true;
    };

    @Autowired
    PaymentMethodRepository dao;

    @Autowired
    PaymentMethodMapper mapper;

    @Override
    protected JpaRepository<PaymentMethodEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected BaseMapper<PaymentMethodEntity, PaymentMethodDto> getMapper() {
        return mapper;
    }

    @Override
    protected IValidator<PaymentMethodEntity> getValidator() {
        return null;
    }

    public List<PaymentMethodEntity> findByUserId(String userId) {
        return dao.findByUserId(userId);
    }


}
