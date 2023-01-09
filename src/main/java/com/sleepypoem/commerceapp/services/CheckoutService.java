package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutMapper;
import com.sleepypoem.commerceapp.repositories.CheckoutRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CheckoutService extends AbstractService<CheckoutDto, CheckoutEntity> {

    final static IValidator<CheckoutEntity> VALIDATE_CHECKOUT = checkout -> {
        return true;
    };

    @Autowired
    CheckoutRepository dao;

    @Autowired
    CheckoutMapper mapper;

    @Override
    protected JpaRepository<CheckoutEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected BaseMapper<CheckoutEntity, CheckoutDto> getMapper() {
        return mapper;
    }

    @Override
    protected IValidator<CheckoutEntity> getValidator() {
        return null;
    }

    public CheckoutEntity getByUserId(String userId) {
        Optional<CheckoutDto> searchedCheckout = dao.findByUserId(userId);
        if (searchedCheckout.isEmpty()) {
            return null;
        }
        return mapper.convertToEntity(searchedCheckout.get());
    }
}
