package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.services.ServicePreconditions;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import com.sleepypoem.commerceapp.services.validators.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class AbstractService<E extends IEntity> implements IService<E> {

    @Override
    public E getOneById(Long id) {
        return getDao().findById(id).orElseThrow(() -> new MyEntityNotFoundException("Entity with id " + id + " not found"));
    }

    @Override
    public List<E> getAll() {
        return getDao().findAll();
    }

    @Override
    public E create(E entity) {
        ServicePreconditions.checkEntityNotNull(entity);
        Validator.validate(getValidator(), entity);
        return getDao().save(entity);
    }

    @Override
    public E update(Long id, E entity) {
        ServicePreconditions.checkEntityNotNull(entity);
        ServicePreconditions.checkExpression(Objects.equals(id, entity.getId()), "Id in URI doesn't match with entity id.");
        Validator.validate(getValidator(), entity);
        return getDao().save(entity);
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

    protected abstract IValidator<E> getValidator();
}
