package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;

public interface IWriteService <D extends IDto, E extends IEntity>{

    D create(E entity) throws MyValidationException;

    D update(Long id, E entity) throws MyValidationException;

    boolean delete(Long id);
}
