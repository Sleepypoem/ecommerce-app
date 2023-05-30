package com.sleepypoem.commerceapp.services.abstracts;

import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import org.springframework.data.domain.Page;

public interface HaveUser<E> {

    /**
     * Get all entities by user id paginated and sorted.
     * @param userId the user id
     * @param page the page number
     * @param size the page size
     * @param sortBy the sort by field
     * @param sortOrder the sort order
     * @return the page of entities
     */
    Page<E> getAllPaginatedAndSortedByUserId(String userId, int page, int size, String sortBy, String sortOrder);
}
