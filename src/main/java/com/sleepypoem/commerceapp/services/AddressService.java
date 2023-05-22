package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.AddressMapper;
import com.sleepypoem.commerceapp.repositories.AddressRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService extends AbstractService<AddressEntity> {

    @Autowired
    IValidator<AddressEntity> validator;
    @Autowired
    AddressRepository dao;

    @Autowired
    AddressMapper mapper;

    public AddressService(IValidator<AddressEntity> validator, AddressRepository dao, AddressMapper mapper) {
        this.validator = validator;
        this.dao = dao;
        this.mapper = mapper;
    }

    @Override
    protected JpaRepository<AddressEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected IValidator<AddressEntity> getValidator() {
        return validator;
    }

    public List<AddressEntity> findByUserId(String userId) {
        return dao.findByUserId(userId);
    }
}
