package com.sleepypoem.commerceapp.controllers.interfaces;

public interface ReadOnlyController<D> {

    public D getOneByIdInternal(Long id);

    public Iterable<D> getAllInternal();


}
