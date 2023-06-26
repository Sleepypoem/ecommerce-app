package com.sleepypoem.commerceapp.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class TestConstants {

    private TestConstants() {
    }

    public static final int DEFAULT_PAGE = 0;

    public static final int DEFAULT_SIZE = 10;

    public static final String DEFAULT_SORT_BY = "id";

    public static final String DEFAULT_SORT_ORDER = "ASC";

    public static final Long DEFAULT_TOTAL_ELEMENTS = 50L;

    public static final Long ZERO_TOTAL_ELEMENTS = 0L;

    public static final Pageable DEFAULT_PAGEABLE = PageRequest.of(DEFAULT_PAGE, DEFAULT_SIZE, Sort.by(DEFAULT_SORT_BY).ascending());
}
