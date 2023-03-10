package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.ProductMapper;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.ProductRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService extends AbstractService<ProductDto, ProductEntity> {

    static final IValidator<ProductEntity> VALIDATE_PRODUCT = product -> {
        if (product.getStock() < 0) {
            return false;
        }

        if (product.getPrice() <= 0) {
            return false;
        }

        return true;
    };

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

    @Override
    protected IValidator<ProductEntity> getValidator() {
        return VALIDATE_PRODUCT;
    }

    public ProductDto modifyStock(Long id, int stock) {
        Optional<ProductEntity> searchedProduct = dao.findById(id);
        if (searchedProduct.isEmpty()) {
            throw new MyEntityNotFoundException("Product with id " + id + " not found");
        }

        ProductEntity product = searchedProduct.get();
        product.setStock(stock);
        return mapper.convertToDto(dao.save(product));
    }
}
