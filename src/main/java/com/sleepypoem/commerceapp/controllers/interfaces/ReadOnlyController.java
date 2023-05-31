package com.sleepypoem.commerceapp.controllers.interfaces;

public interface ReadOnlyController<D, ID> {

    D getOneByIdInternal(ID id);

    Iterable<D> getAllInternal();


}
