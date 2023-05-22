package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.repositories.ProductRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService extends AbstractService<ProductEntity> {

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

    @Override
    protected JpaRepository<ProductEntity, Long> getDao() {
        return dao;
    }

    @Override
    protected IValidator<ProductEntity> getValidator() {
        return VALIDATE_PRODUCT;
    }

    public ProductEntity modifyStock(Long id, int stock) {
        ProductEntity product = getOneById(id);
        product.setStock(stock);
        return dao.save(product);
    }
}
