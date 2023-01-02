package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.ProductMapper;
import com.sleepypoem.commerceapp.repositories.ProductRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends AbstractService<ProductDto, ProductEntity> {

    @Autowired
    ProductRepository dao;

    @Autowired
    ProductMapper mapper;
    @Override
    protected JpaRepository<ProductEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected BaseMapper<ProductEntity, ProductDto> getMapper() {
        return mapper;
    }
}
