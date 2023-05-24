package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.annotations.ValidableMethod;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.services.ServicePreconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

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
    @ValidableMethod
    @Transactional
    public E create(E entity) {
        ServicePreconditions.checkEntityNotNull(entity);
        return getDao().save(entity);
    }

    @Override
    @ValidableMethod
    @Transactional
    public E update(Long id, E entity) {
        ServicePreconditions.checkEntityNotNull(entity);
        ServicePreconditions.checkExpression(Objects.equals(id, entity.getId()), "Id in URI doesn't match with entity id.");
        return getDao().save(entity);
    }

    @Override
    public boolean delete(Long id) {
        E entity = getOneById(id);
        try {
            getDao().delete(entity);
            return true;
        } catch (Exception e) {
            log.error("Error while deleting entity with id {}.", id);
            return false;
        }
    }

    protected abstract JpaRepository<E, Long> getDao();
}
