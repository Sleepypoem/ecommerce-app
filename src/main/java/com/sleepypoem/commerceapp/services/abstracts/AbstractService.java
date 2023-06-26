package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.annotations.ValidableMethod;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.services.ServicePreconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class AbstractService<E extends IEntity<?>, ID> implements IService<E, ID> {

    @Override
    public E getOneById(ID id) {
        return getDao().findById(id).orElseThrow(() -> new MyEntityNotFoundException(getEntityName() + " with id " + id + " not found"));
    }

    /**
     * Gets the name of the entity by removing "service" from the name of the class inheriting this class.
     *
     * @return the name of the entity
     */
    private String getEntityName() {
        String className = this.getClass().getSimpleName();
        return className.substring(0, className.length() - 7);
    }

    /**
     * Gets all the entities paginated and sorted.
     *
     * @param page      the page number
     * @param size      the size of the page
     * @param sortBy    the field to sort by
     * @param sortOrder the sort order
     * @return the page of entities
     */
    @Transactional(readOnly = true)
    public Page<E> getAllPaginatedAndSorted(int page, int size, String sortBy, String sortOrder) {
        return getDao().findAll(PageRequest.of(page, size, createSort(sortBy, sortOrder)));
    }

    @Override
    @Transactional(readOnly = true)
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
    public E update(ID id, E entity) {
        ServicePreconditions.checkEntityNotNull(entity);
        ServicePreconditions.checkExpression(Objects.equals(id, entity.getId()), "Id in URI doesn't match with entity id.");
        return getDao().save(entity);
    }

    @Override
    @Transactional
    public boolean deleteById(ID id) {
        E entity = getOneById(id);
        return delete(entity);
    }

    @Override
    @Transactional
    public boolean delete(E entity) {
        try {
            getDao().delete(entity);
            return true;
        } catch (Exception e) {
            log.error("Error while deleting {}. Error: {}", getEntityName(), e.getMessage());
            return false;
        }
    }

    /**
     * Creates a sort object from the given parameters.
     *
     * @param sortBy    the field to sort by
     * @param sortOrder the sort order
     * @return the sort object
     */
    protected Sort createSort(String sortBy, String sortOrder) {
        return Sort.by(Sort.Direction.fromString(sortOrder), sortBy);
    }

    protected abstract JpaRepository<E, ID> getDao();
}
