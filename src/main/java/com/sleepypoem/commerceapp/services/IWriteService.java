package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;

public interface IWriteService <D extends IDto, E extends IEntity>{

    D create(E entity);

    D update(Long id, E entity);

    boolean delete(Long id);
}
