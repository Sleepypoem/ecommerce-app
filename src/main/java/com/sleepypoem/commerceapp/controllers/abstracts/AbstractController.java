package com.sleepypoem.commerceapp.controllers.abstracts;

import com.sleepypoem.commerceapp.controllers.RequestPreconditions;
import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class AbstractController<D extends IDto<?>, E extends IEntity<?>, ID> extends AbstractReadOnlyController<D, E, ID> {


    protected AbstractController(BaseMapper<E, D> mapper) {
        super(mapper);
    }

    public D createInternal(E entity) {
        RequestPreconditions.checkNotNull(entity);
        E createdEntity = getService().create(entity);
        return mapper.convertToDto(createdEntity);
    }

    public D updateInternal(ID id, E entity) {
        RequestPreconditions.checkRequestElementNotNull(entity);
        RequestPreconditions.checkRequestState(Objects.equals(id, entity.getId()), "Id in URI doesn't match resource Id.");
        E updatedEntity = getService().update(id, entity);
        return mapper.convertToDto(updatedEntity);
    }

    public boolean deleteInternal(ID id) {
        return getService().delete(id);
    }

}
