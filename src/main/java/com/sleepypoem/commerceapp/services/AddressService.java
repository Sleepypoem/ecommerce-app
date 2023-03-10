package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.AddressMapper;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.repositories.AddressRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService extends AbstractService<AddressDto, AddressEntity> {

    @Autowired
    IValidator<AddressEntity> validateAddress;
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

    @Override
    protected IValidator<AddressEntity> getValidator() {
        return validateAddress;
    }

    public List<AddressEntity> findByUserId(String userId) {
        return dao.findByUserId(userId);
    }
}
