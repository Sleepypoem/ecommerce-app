package com.sleepypoem.commerceapp.controllers.utils;

import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaginatorTest {

    Paginator<ProductEntity, ProductDto> paginator;

    BaseMapper<ProductEntity, ProductDto> mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductMapper();
        paginator = new Paginator<>(mapper);
    }

    @Test
    void testGetPaginatedDto() {
        //arrange
        List<ProductEntity> entities = List.of(
                ProductEntity.builder().id(1L).name("testProduct").price(100.0).stock(100).build()
        );
        Page<ProductEntity> pagedEntity = new PageImpl<>(entities);

        //act
        PaginatedDto<ProductDto> paginatedDto = paginator.getPaginatedDto(pagedEntity, "products");

        //assert
        assertAll(
                () -> assertEquals(0, paginatedDto.getCurrentPage()),
                () -> assertEquals(1, paginatedDto.getTotalElements()),
                () -> assertEquals(1, paginatedDto.getContent().size()),
                () -> assertNull(paginatedDto.getPreviousPage()),
                () -> assertEquals("testProduct", paginatedDto.getContent().get(0).getName()),
                () -> assertEquals(100.0, paginatedDto.getContent().get(0).getPrice()),
                () -> assertEquals(100, paginatedDto.getContent().get(0).getStock())
        );
    }

}