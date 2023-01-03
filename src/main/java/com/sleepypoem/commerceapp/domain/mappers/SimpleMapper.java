package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import org.springframework.beans.BeanUtils;

public abstract class SimpleMapper <D extends IDto, E extends IEntity> extends BaseMapper<E,D> {

    public abstract D getDtoInstance();

    public abstract E getEntityInstance();
    @Override
    public E convertToEntity(D dto) {
        E entity = getEntityInstance();
        if (dto != null) {
            BeanUtils.copyProperties(dto,entity);
        }
        return entity;
    }

    @Override
    public D convertToDto(E entity) {
        D dto = getDtoInstance();
        if(entity != null){
            BeanUtils.copyProperties(entity, dto);
        }
        return dto;
    }
}
