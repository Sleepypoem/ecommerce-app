package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;

public interface IService<E extends IEntity, ID> extends IReadService<E, ID>, IWriteService<E, ID> {
}
