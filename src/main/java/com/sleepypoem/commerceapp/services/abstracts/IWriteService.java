package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;

public interface IWriteService<D extends IDto, E extends IEntity> {

    D create(E entity) throws MyEntityNotFoundException, MyValidationException;

    D update(Long id, E entity) throws MyBadRequestException, MyValidationException;

    boolean delete(Long id);
}
