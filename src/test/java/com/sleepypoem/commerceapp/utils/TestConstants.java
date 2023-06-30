package com.sleepypoem.commerceapp.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TestConstants {

    private TestConstants() {
    }

    public static final int DEFAULT_FIRST_PAGE = 0;

    public static final int DEFAULT_LAST_PAGE = 4;

    public static final int DEFAULT_SIZE = 10;

    public static final String DEFAULT_SORT_BY = "id";

    public static final String DEFAULT_SORT_ORDER = "ASC";

    public static final Long DEFAULT_TOTAL_ELEMENTS = 50L;

    public static final Long ZERO_TOTAL_ELEMENTS = 0L;

    public static final Pageable DEFAULT_PAGEABLE_AT_FIRST_PAGE = PageRequest.of(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, Sort.by(DEFAULT_SORT_BY).ascending());

    public static final Pageable DEFAULT_PAGEABLE_AT_LAST_PAGE = PageRequest.of(DEFAULT_LAST_PAGE, DEFAULT_SIZE, Sort.by(DEFAULT_SORT_BY).ascending());
}
