package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper extends BaseMapper<ProductEntity, ProductDto> {
    @Override
    public ProductEntity convertToEntity(ProductDto dto) {
        ProductEntity entity = new ProductEntity();
        if(dto != null){
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public ProductDto convertToDto(ProductEntity entity) {
        ProductDto dto = new ProductDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
