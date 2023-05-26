package com.sleepypoem.commerceapp.controllers.interfaces;

public interface ReadOnlyController<D> {

    D getOneByIdInternal(Long id);

    Iterable<D> getAllInternal();


}
