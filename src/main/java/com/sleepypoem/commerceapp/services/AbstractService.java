package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractService<D extends IDto, E extends IEntity> implements IService<D, E> {

    @Override
    public Optional<D> getOneById(Long id) {
        Optional<D> dto = Optional.empty();
        E entity;
        Optional<E> isEntityPresent = getDao().findById(id);
        if (isEntityPresent.isPresent()) {
            entity = isEntityPresent.get();
            dto = Optional.of(getMapper().convertToDto(entity));
        }
        return dto;
    }

    @Override
    public List<D> getAll() {
        List<E> entities = getDao().findAll();
        return getMapper().convertToDtoList(entities);
    }

    @Override
    public D create(E entity) {
        E persisted = getDao().save(entity);
        return getMapper().convertToDto(persisted);
    }

    @Override
    public D update(Long id, E entity) {
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
}
