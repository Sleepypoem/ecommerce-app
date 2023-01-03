package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutMapper;
import com.sleepypoem.commerceapp.repositories.CheckoutRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

public class CheckoutService extends AbstractService<CheckoutDto, CheckoutEntity> {

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
}
