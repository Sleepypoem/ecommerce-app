package com.sleepypoem.commerceapp.domain.dto;

import lombok.Data;

import java.util.List;

@Data
public class PaginatedDto<D> {

    private Integer currentPage;

    private String previousPage;

    private String nextPage;

    private Long totalElements;

    private List<D> content;
}
