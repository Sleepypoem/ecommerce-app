package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.interfaces.IEntity;

public interface IWriteService<E extends IEntity, ID> {

    /**
     * Creates an entity.
     *
     * @param entity the entity to create
     * @return the created entity
     */
    E create(E entity);

    /**
     * Updates an entity.
     *
     * @param id     the id of the entity to update
     * @param entity the entity to update
     * @return the updated entity
     */
    E update(ID id, E entity);

    /**
     * Deletes an entity referenced by the entity id.
     *
     * @param id the id of the entity to delete
     * @return true if the entity was deleted, false otherwise
     */
    boolean deleteById(ID id);

    /**
     * Deletes an entity.
     *
     * @param entity the entity to delete
     * @return true if the entity was deleted, false otherwise
     */
    boolean delete(E entity);
}
