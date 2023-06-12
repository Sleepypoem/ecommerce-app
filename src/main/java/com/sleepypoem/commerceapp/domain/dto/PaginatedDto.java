package com.sleepypoem.commerceapp.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PaginatedDto<D> {

    private Integer currentPage;

    private String previousPage;

    private String nextPage;

    private Long totalElements;

    private List<D> content;
}
