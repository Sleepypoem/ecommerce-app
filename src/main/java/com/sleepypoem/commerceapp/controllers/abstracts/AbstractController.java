package com.sleepypoem.commerceapp.controllers.abstracts;

import com.sleepypoem.commerceapp.controllers.RequestPreconditions;
import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public abstract class AbstractController<D extends IDto, E extends IEntity> extends AbstractReadOnlyController<D, E> {

    public D createInternal(E entity) throws Exception {
        log.info(entity.toString());
        RequestPreconditions.checkNotNull(entity);
        return getService().create(entity);
    }

    public D updateInternal(Long id, E entity) throws Exception {
        RequestPreconditions.checkRequestElementNotNull(entity);
        RequestPreconditions.checkRequestState(entity.getId() == null);
        RequestPreconditions.checkRequestState(!Objects.equals(id, entity.getId()), "Id in URI doesn't match resource Id.");
        return getService().update(id, entity);
    }

    public boolean deleteInternal(Long id) {
        return getService().delete(id);
    }
}
