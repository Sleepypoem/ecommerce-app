package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.repositories.AddressRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.impl.ValidateAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Validable(ValidateAddress.class)
public class AddressService extends AbstractService<AddressEntity> {
    AddressRepository dao;

    public AddressService(AddressRepository dao) {
        this.dao = dao;
    }

    @Override
    protected JpaRepository<AddressEntity, Long> getDao() {
        return dao;
    }

    public List<AddressEntity> findByUserId(String userId) {
        return dao.findByUserId(userId);
    }
}
