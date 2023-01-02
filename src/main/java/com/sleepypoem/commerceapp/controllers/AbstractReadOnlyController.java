package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;

import java.util.List;
import java.util.Optional;

public abstract class AbstractReadOnlyController <D extends IDto, E extends IEntity> {

    protected abstract AbstractService<D, E> getService();

    public List<D> getAllInternal(){
        return getService().getAll();
    }

    public Optional<D> getOneByIdInternal(Long id){
        return getService().getOneById(id);
    }
}
