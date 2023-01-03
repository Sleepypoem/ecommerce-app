package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class CheckoutMapper extends BaseMapper<CheckoutEntity, CheckoutDto> {
    @Override
    public CheckoutEntity convertToEntity(CheckoutDto dto) {
        CheckoutEntity entity = new CheckoutEntity();
        if(dto != null){
            BeanUtils.copyProperties(dto, entity);
        }
        return entity;
    }

    @Override
    public CheckoutDto convertToDto(CheckoutEntity entity) {
        CheckoutDto dto = new CheckoutDto();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
