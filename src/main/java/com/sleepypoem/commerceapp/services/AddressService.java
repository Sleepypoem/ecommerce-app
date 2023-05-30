package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.repositories.AddressRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.abstracts.HaveUser;
import com.sleepypoem.commerceapp.services.validators.impl.ValidateAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Validable(ValidateAddress.class)
public class AddressService extends AbstractService<AddressEntity> implements HaveUser<AddressEntity> {
    AddressRepository dao;

    public AddressService(AddressRepository dao) {
        this.dao = dao;
    }

    @Override
    protected JpaRepository<AddressEntity, Long> getDao() {
        return dao;
    }

    public Page<AddressEntity> findByUserId(String userId, final int page, final int size, final String sort, final String direction) {
        return dao.findByUserId(userId, PageRequest.of(page, size, createSort(sort, direction)));
    }

    @Override
    public Page<AddressEntity> getAllPaginatedAndSortedByUserId(String userId, int page, int size, String sortBy, String sortOrder) {
        return dao.findByUserId(userId, PageRequest.of(page, size, createSort(sortBy, sortOrder)));
    }
}
