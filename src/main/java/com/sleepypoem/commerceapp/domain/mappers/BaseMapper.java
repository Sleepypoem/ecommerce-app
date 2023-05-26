package com.sleepypoem.commerceapp.domain.mappers;

import java.util.Collection;
import java.util.List;

public abstract class BaseMapper<E, D> {
    protected BaseMapper() {
    }

    /**
     * Convert a DTO to an entity.
     * @param dto the DTO to convert.
     * @return the entity.
     */
    public abstract E convertToEntity(D dto);

    /**
     * Convert an entity to a DTO.
     * @param entity the entity to convert.
     * @return the DTO.
     */
    public abstract D convertToDto(E entity);

    /**
     * Converts a collection of DTOs to a collection of entities.
     *
     * @param dto the collection of DTOs to convert
     * @return the collection of entities
     */
    public Collection<E> convertToEntity(Collection<D> dto) {
        return dto.stream().map(this::convertToEntity).toList();
    }

    /**
     * Converts a collection of entities to a collection of DTOs.
     *
     * @param entity the collection of entities to convert
     * @return the collection of DTOs
     */
    public Collection<D> convertToDto(Collection<E> entity) {
        return entity.stream().map(this::convertToDto).toList();
    }

    /**
     * Converts a collection of DTOs to a list of entities.
     *
     * @param dto the collection of DTOs to convert
     * @return the list of entities
     */
    public List<E> convertToEntityList(Collection<D> dto) {
        return this.convertToEntity(dto).stream().toList();
    }

    /**
     * Converts a collection of entities to a list of DTOs.
     *
     * @param entity the collection of entities to convert
     * @return the list of DTOs
     */
    public List<D> convertToDtoList(Collection<E> entity) {
        return this.convertToDto(entity).stream().toList();
    }
}
