package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;

public interface IService<E extends IEntity> extends IReadService<E>, IWriteService<E> {
}
