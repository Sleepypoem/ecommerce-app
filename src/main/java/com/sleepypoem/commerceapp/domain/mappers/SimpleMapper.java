package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import org.modelmapper.ModelMapper;

public abstract class SimpleMapper<D extends AbstractDto, E extends AbstractEntity> extends BaseMapper<E, D> {

    private final ModelMapper modelMapper = new ModelMapper();

    public abstract D getDtoInstance();

    public abstract E getEntityInstance();

    @Override
    public E convertToEntity(D dto) {
        E entity = getEntityInstance();
        if (dto != null) {
            modelMapper.map(dto, entity);
        }
        return entity;
    }

    @Override
    public D convertToDto(E entity) {
        D dto = getDtoInstance();
        if (entity != null) {
            modelMapper.map(entity, dto);
        }
        return dto;
    }
}
