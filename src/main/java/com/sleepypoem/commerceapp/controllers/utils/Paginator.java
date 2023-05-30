package com.sleepypoem.commerceapp.controllers.utils;

import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import org.springframework.data.domain.Page;

public class Paginator<E, D>{

    private final BaseMapper<E, D> mapper;

    public Paginator(BaseMapper<E, D> mapper) {
        this.mapper = mapper;
    }

    public PaginatedDto<D> getPaginatedDto(Page<E> pagedEntity, String resourceName) {
        PaginatedDto<D> paginatedDto = new PaginatedDto<>();
        paginatedDto.setCurrentPage(pagedEntity.getNumber());
        paginatedDto.setTotalElements(pagedEntity.getTotalElements());
        paginatedDto.setContent(mapper.convertToDtoList(pagedEntity.getContent()));
        if (pagedEntity.hasPrevious()) {
            paginatedDto.setPreviousPage("/api/" + resourceName + "?page=" + pagedEntity.previousPageable().getPageNumber() + "&size=" + pagedEntity.previousPageable().getPageSize());
        }
        if (pagedEntity.hasNext()) {
            paginatedDto.setNextPage("/api/" + resourceName + "?page=" + pagedEntity.nextPageable().getPageNumber() + "&size=" + pagedEntity.nextPageable().getPageSize());
        }
        return paginatedDto;
    }
}
