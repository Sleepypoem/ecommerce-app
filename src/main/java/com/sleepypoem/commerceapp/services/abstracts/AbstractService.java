package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;
import com.sleepypoem.commerceapp.services.ServicePreconditions;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import com.sleepypoem.commerceapp.services.validators.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public abstract class AbstractService<D extends IDto, E extends IEntity> implements IService<D, E> {

    @Override
    public D getOneById(Long id) {
        E entity;
        Optional<E> isEntityPresent = getDao().findById(id);

        if (isEntityPresent.isPresent()) {
            entity = isEntityPresent.get();

        } else {
            throw new MyEntityNotFoundException(getClass().getSimpleName() + " Entity with id " + id + " not found.");

        }
        return getMapper().convertToDto(entity);
    }

    @Override
    public List<D> getAll() {
        List<E> entities = getDao().findAll();
        return getMapper().convertToDtoList(entities);
    }

    @Override
    public D create(E entity) throws MyEntityNotFoundException, MyValidationException {
        ServicePreconditions.checkEntityNotNull(entity);
        Validator.validate(getValidator(), entity);
        E persisted = getDao().save(entity);
        return getMapper().convertToDto(persisted);
    }

    @Override
    public D update(Long id, E entity) throws MyBadRequestException, MyValidationException {
        ServicePreconditions.checkEntityNotNull(entity);
        ServicePreconditions.checkExpression(Objects.equals(id, entity.getId()), "Id in URI doesn't match with entity id.");
        Validator.validate(getValidator(), entity);
        E updated = getDao().save(entity);
        return getMapper().convertToDto(updated);
    }

    @Override
    public boolean delete(Long id) {
        try {
            getDao().deleteById(id);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    protected abstract JpaRepository<E, Long> getDao();

    protected abstract BaseMapper<E, D> getMapper();

    protected abstract IValidator<E> getValidator();
}
