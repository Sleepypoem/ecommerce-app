package com.sleepypoem.commerceapp.services.abstracts;

import java.util.List;

public interface IReadService<E, ID> {

    /**
     * Gets an entity by id.
     *
     * @param id the id of the entity to get
     * @return the entity
     */
    E getOneById(ID id);

    /**
     * Gets all the entities
     *
     * @return the list of entities
     */
    List<E> getAll();
}
