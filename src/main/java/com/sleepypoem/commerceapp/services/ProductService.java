package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.repositories.ProductRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import com.sleepypoem.commerceapp.services.validators.impl.ValidateProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Validable(ValidateProduct.class)
public class ProductService extends AbstractService<ProductEntity> {

    ProductRepository dao;

    @Override
    protected JpaRepository<ProductEntity, Long> getDao() {
        return dao;
    }

    public ProductService(ProductRepository dao, IValidator<ProductEntity> validator) {
        this.dao = dao;
    }

    @Transactional
    public ProductEntity modifyStock(Long id, int stock) {
        ProductEntity product = getOneById(id);
        product.setStock(stock);
        return update(id, product);
    }
}
