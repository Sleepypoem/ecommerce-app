package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.annotations.Validable;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.repositories.ProductRepository;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import com.sleepypoem.commerceapp.services.validators.impl.ValidateProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
@Validable(ValidateProduct.class)
public class ProductService extends AbstractService<ProductEntity, Long> {

    ProductRepository dao;

    @Override
    protected JpaRepository<ProductEntity, Long> getDao() {
        return dao;
    }

    public ProductService(ProductRepository dao) {
        this.dao = dao;
    }

    public ProductEntity increaseStock(Long id, int stock) {
        ProductEntity product = getOneById(id);
        product.setStock(stock + product.getStock());
        return update(id, product);
    }

    public ProductEntity decreaseStock(Long id, int stock) {
        ProductEntity product = getOneById(id);
        product.setStock(product.getStock() - stock);
        return update(id, product);
    }
}
