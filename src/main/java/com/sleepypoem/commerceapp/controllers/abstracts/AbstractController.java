package com.sleepypoem.commerceapp.controllers.abstracts;

import com.sleepypoem.commerceapp.controllers.RequestPreconditions;
import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;

public abstract class AbstractController <D extends IDto, E extends IEntity> extends AbstractReadOnlyController<D, E>{

    public D createInternal(E entity) throws MyValidationException {
        RequestPreconditions.checkNotNull(entity);
        RequestPreconditions.checkRequestState(entity.getId() == null);
        return getService().create(entity);
    }

    public D updateInternal(Long id, E entity) throws MyValidationException {
        RequestPreconditions.checkRequestElementNotNull(entity);
        RequestPreconditions.checkRequestState(entity.getId() == null);
        RequestPreconditions.checkRequestState(id != entity.getId(), "Id in URI doesn't match resource Id.");
        return getService().update(id, entity);
    }

    public boolean deleteInternal(Long id){
        return getService().delete(id);
    }
}
