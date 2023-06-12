package com.sleepypoem.commerceapp.controllers.abstracts;

import com.sleepypoem.commerceapp.controllers.interfaces.ReadOnlyController;
import com.sleepypoem.commerceapp.controllers.utils.Paginator;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;

import java.util.List;

public abstract class AbstractReadOnlyController<D extends IDto<?>, E extends IEntity<?>, ID> implements ReadOnlyController<D, ID> {

    protected abstract AbstractService<E, ID> getService();

    protected BaseMapper<E, D> mapper;

    protected AbstractReadOnlyController(BaseMapper<E, D> mapper) {
        this.mapper = mapper;
    }

    @Override
    public D getOneByIdInternal(ID id) {
        return mapper.convertToDto(getService().getOneById(id));
    }

    @Override
    public Iterable<D> getAllInternal() {
        List<E> entities = getService().getAll();
        return mapper.convertToDto(entities);
    }

    public PaginatedDto<D> getAllPaginatedAndSortedInternal(int page, int size, String sortBy, String sortOrder, String resourceName) {
        Paginator<D> paginator = new Paginator<>(resourceName);
        return paginator.getPaginatedDtoFromPage(getService().getAllPaginatedAndSorted(page, size, sortBy, sortOrder), mapper);
    }
}
