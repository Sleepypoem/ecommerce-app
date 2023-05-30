package com.sleepypoem.commerceapp.services.abstracts;

import java.util.List;

public interface IReadService<E> {

    /**
     * Gets an entity by id.
     * @param id the id of the entity to get
     * @return the entity
     */
    E getOneById(Long id);

    /**
     * Gets all the entities
     * @return the list of entities
     */
    List<E> getAll();
}
