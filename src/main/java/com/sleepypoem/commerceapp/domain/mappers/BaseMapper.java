package com.sleepypoem.commerceapp.domain.mappers;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseMapper<E, D> {
    public abstract E convertToEntity(D dto);

    public abstract D convertToDto(E entity);

    public Collection<E> convertToEntity(Collection<D> dto) {
        return dto.stream().map(d -> convertToEntity(d)).collect(Collectors.toList());
    }

    public Collection<D> convertToDto(Collection<E> entity) {
        return entity.stream().map(e -> convertToDto(e)).collect(Collectors.toList());
    }

    public List<E> convertToEntityList(Collection<D> dto) {
        return convertToEntity(dto).stream().collect(Collectors.toList());
    }

    public List<D> convertToDtoList(Collection<E> entity) {
        return convertToDto(entity).stream().collect(Collectors.toList());
    }
}