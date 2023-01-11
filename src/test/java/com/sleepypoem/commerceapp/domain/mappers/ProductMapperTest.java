package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ProductMapperTest {

    ProductMapper mapper;

    @BeforeEach
    void init() {
        mapper = new ProductMapper();
    }

    @Test
    void testMethodReturnsEntityInstance() {
        ProductEntity entity = mapper.getEntityInstance();
        assertNotNull(entity);
    }

    @Test
    void testMethodReturnsDtoInstance() {
        ProductDto dto = mapper.getDtoInstance();
        assertNotNull(dto);
    }

    @Test
    void testMapEntityToDto() {
        ProductEntity entity = ProductEntity.
                builder()
                .id(1L)
                .build();

        ProductDto mappedDto = mapper.convertToDto(entity);
        Long expected = entity.getId();
        Long actual = mappedDto.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoToEntity() {
        ProductDto dto = ProductDto
                .builder()
                .id(1L)
                .build();

        ProductEntity mappedEntity = mapper.convertToEntity(dto);
        Long expected = dto.getId();
        Long actual = mappedEntity.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoListToEntityList() {
        ArrayList<ProductDto> dtoList = new ArrayList<>();
        dtoList.add(ProductDto.builder().id(1L).build());
        dtoList.add(ProductDto.builder().id(2L).build());

        ArrayList<ProductEntity> mappedEntityList = (ArrayList<ProductEntity>) mapper.convertToEntity(dtoList);
        String expected = dtoList.get(0).toString();
        String actual = mappedEntityList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapEntityListToDtoList() {
        ArrayList<ProductEntity> entityList = new ArrayList<>();
        entityList.add(ProductEntity.builder().id(1L).build());
        entityList.add(ProductEntity.builder().id(2L).build());

        ArrayList<ProductDto> mappedDtoList = (ArrayList<ProductDto>) mapper.convertToDto(entityList);
        String expected = entityList.get(0).toString();
        String actual = mappedDtoList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }
}
