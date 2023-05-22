package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;

public interface IWriteService<E extends IEntity> {

    E create(E entity);

    E update(Long id, E entity);

    boolean delete(Long id);
}
