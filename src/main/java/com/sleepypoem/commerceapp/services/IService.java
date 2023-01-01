package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;

public interface IService<D extends IDto, E extends IEntity> extends IReadService<D>, IWriteService<D, E> {
}
