package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.services.AbstractService;

public abstract class AbstractController <D extends IDto, E extends IEntity> extends AbstractReadOnlyController<D, E>{

    public D createInternal(E entity){
        return getService().create(entity);
    }

    public D updateInternal(Long id, E entity){
        return getService().update(id, entity);
    }

    public boolean deleteInternal(Long id){
        return getService().delete(id);
    }
}
