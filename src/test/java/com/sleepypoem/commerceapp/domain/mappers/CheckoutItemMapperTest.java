package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CheckoutItemMapperTest {

    CheckoutItemMapper mapper;

    @BeforeEach
    void init() {
        mapper = new CheckoutItemMapper();
    }

    @Test
    void testMethodReturnsEntityInstance() {
        CheckoutItemEntity entity = mapper.getEntityInstance();
        assertNotNull(entity);
    }

    @Test
    void testMethodReturnsDtoInstance() {
        CheckoutItemDto dto = mapper.getDtoInstance();
        assertNotNull(dto);
    }

    @Test
    void testMapEntityToDto() {
        CheckoutItemEntity entity = CheckoutItemEntity.
                builder()
                .id(1L)
                .build();

        CheckoutItemDto mappedDto = mapper.convertToDto(entity);
        Long expected = entity.getId();
        Long actual = mappedDto.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoToEntity() {
        CheckoutItemDto dto = CheckoutItemDto
                .builder()
                .id(1L)
                .build();

        CheckoutItemEntity mappedEntity = mapper.convertToEntity(dto);
        Long expected = dto.getId();
        Long actual = mappedEntity.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoListToEntityList() {
        ArrayList<CheckoutItemDto> dtoList = new ArrayList<>();
        dtoList.add(CheckoutItemDto.builder().id(1L).build());
        dtoList.add(CheckoutItemDto.builder().id(2L).build());

        ArrayList<CheckoutItemEntity> mappedEntityList = (ArrayList<CheckoutItemEntity>) mapper.convertToEntity(dtoList);
        String expected = dtoList.get(0).toString();
        String actual = mappedEntityList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapEntityListToDtoList() {
        ArrayList<CheckoutItemEntity> entityList = new ArrayList<>();
        entityList.add(CheckoutItemEntity.builder().id(1L).build());
        entityList.add(CheckoutItemEntity.builder().id(2L).build());

        ArrayList<CheckoutItemDto> mappedDtoList = (ArrayList<CheckoutItemDto>) mapper.convertToDto(entityList);
        String expected = entityList.get(0).toString();
        String actual = mappedDtoList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }
}
