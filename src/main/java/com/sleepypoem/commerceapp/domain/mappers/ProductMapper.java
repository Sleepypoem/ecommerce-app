package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends SimpleMapper<ProductDto, ProductEntity> {

    @Override
    public ProductDto getDtoInstance() {
        return new ProductDto();
    }

    @Override
    public ProductEntity getEntityInstance() {
        return new ProductEntity();
    }
}
