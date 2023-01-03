package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.AddressMapper;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.repositories.AddressRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;


public class AddressService extends AbstractService<AddressDto, AddressEntity> {
    @Autowired
    AddressRepository dao;

    @Autowired
    AddressMapper mapper;

    @Override
    protected JpaRepository<AddressEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected BaseMapper<AddressEntity, AddressDto> getMapper() {
        return mapper;
    }
}
