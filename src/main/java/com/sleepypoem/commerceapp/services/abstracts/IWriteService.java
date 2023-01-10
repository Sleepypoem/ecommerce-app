package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;

public interface IWriteService<D extends IDto, E extends IEntity> {

    D create(E entity) throws Exception;

    D update(Long id, E entity) throws Exception;

    boolean delete(Long id);
}
