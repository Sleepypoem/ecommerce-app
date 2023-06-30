package com.sleepypoem.commerceapp.controllers.utils;

import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Objects;

public class Paginator<D> {

    public static final String API_PATH = "/api/";

    public static final String PAGE = "page=";

    public static final String SIZE = "size=";

    private final String resourceName;

    public Paginator(String resourceName) {
        this.resourceName = resourceName;
    }

    public <E> PaginatedDto<D> getPaginatedDtoFromPage(Page<E> pagedEntity, BaseMapper<E, D> mapper) {
        PaginatedDto<D> paginatedDto = new PaginatedDto<>();
        paginatedDto.setCurrentPage(pagedEntity.getNumber());
        paginatedDto.setTotalElements(pagedEntity.getTotalElements());
        paginatedDto.setContent(mapper.convertToDtoList(pagedEntity.getContent()));
        if (pagedEntity.hasPrevious()) {
            paginatedDto.setPreviousPage(createPreviousPageUrl(pagedEntity));
        }
        if (pagedEntity.hasNext()) {
            paginatedDto.setNextPage(createNextPageUrl(pagedEntity));
        }
        return paginatedDto;
    }

    public <E> String createNextPageUrl(Page<E> page) {
        Pageable nextPageable = page.nextPageable();
        int pageNumber = nextPageable.getPageNumber();
        int pageSize = nextPageable.getPageSize();
        Sort.Order sortOrder = nextPageable.getSort().getOrderFor("id");

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(API_PATH)
                .append(resourceName)
                .append(PAGE)
                .append(pageNumber)
                .append("&")
                .append(SIZE)
                .append(pageSize)
                .append("&sortBy=")
                .append(sortOrder.getProperty())
                .append("&sortOrder=")
                .append(sortOrder.getDirection());

        return urlBuilder.toString();
    }

    public String createPreviousPageUrl(Page<?> page) {
        Pageable previousPageable = page.previousPageable();
        int pageNumber = previousPageable.getPageNumber();
        int pageSize = previousPageable.getPageSize();
        Sort.Order sortOrder = previousPageable.getSort().getOrderFor("id");

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(API_PATH)
                .append(resourceName)
                .append(PAGE)
                .append(pageNumber)
                .append("&")
                .append(SIZE)
                .append(pageSize)
                .append("&sortBy=")
                .append(sortOrder.getProperty())
                .append("&sortOrder=")
                .append(sortOrder.getDirection());

        return urlBuilder.toString();
    }


    public PaginatedDto<D> getPaginatedDtoFromList(Integer currentPage, Integer size, Long totalElements, List<D> content) {
        PaginatedDto<D> paginatedDto = new PaginatedDto<>();
        String nextPage = Objects.equals(Long.valueOf(currentPage), totalElements) ? null : API_PATH + resourceName + "&" + PAGE + (currentPage + 1) + "&" + SIZE + size;
        String previousPage = currentPage == 0 ? null : API_PATH + resourceName + "&" + PAGE + (currentPage - 1) + "&" + SIZE + size;
        paginatedDto.setContent(content);
        paginatedDto.setTotalElements(totalElements);
        paginatedDto.setCurrentPage(currentPage);
        paginatedDto.setNextPage(nextPage);
        paginatedDto.setPreviousPage(previousPage);
        return paginatedDto;
    }
}
