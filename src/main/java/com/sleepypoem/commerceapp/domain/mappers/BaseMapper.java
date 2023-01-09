package com.sleepypoem.commerceapp.domain.mappers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseMapper<E, D> {
    public BaseMapper() {
    }

    public abstract E convertToEntity(D dto);

    public abstract D convertToDto(E entity);

    public Collection<E> convertToEntity(Collection<D> dto) {
        Collection<E> res = (Collection) dto.stream().map((d) -> {
            return this.convertToEntity(d);
        }).collect(Collectors.toList());

        for (D d : dto) {
            System.out.println(d.toString());
        }
        return res;
    }

    public Collection<D> convertToDto(Collection<E> entity) {
        return (Collection) entity.stream().map((e) -> {
            return this.convertToDto(e);
        }).collect(Collectors.toList());
    }

    public List<E> convertToEntityList(Collection<D> dto) {
        return (List) this.convertToEntity(dto).stream().collect(Collectors.toList());
    }

    public List<D> convertToDtoList(Collection<E> entity, Object... args) {
        return (List) this.convertToDto(entity).stream().collect(Collectors.toList());
    }
}
