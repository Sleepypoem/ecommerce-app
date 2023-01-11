package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CheckoutMapperTest {

    CheckoutMapper mapper;

    @BeforeEach
    void init() {
        mapper = new CheckoutMapper();
    }

    @Test
    void testMethodReturnsEntityInstance() {
        CheckoutEntity entity = mapper.getEntityInstance();
        assertNotNull(entity);
    }

    @Test
    void testMethodReturnsDtoInstance() {
        CheckoutDto dto = mapper.getDtoInstance();
        assertNotNull(dto);
    }

    @Test
    void testMapEntityToDto() {
        CheckoutEntity entity = CheckoutEntity.
                builder()
                .id(1L)
                .build();

        CheckoutDto mappedDto = mapper.convertToDto(entity);
        Long expected = entity.getId();
        Long actual = mappedDto.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoToEntity() {
        CheckoutDto dto = CheckoutDto
                .builder()
                .id(1L)
                .build();

        CheckoutEntity mappedEntity = mapper.convertToEntity(dto);
        Long expected = dto.getId();
        Long actual = mappedEntity.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoListToEntityList() {
        ArrayList<CheckoutDto> dtoList = new ArrayList<>();
        dtoList.add(CheckoutDto.builder().id(1L).build());
        dtoList.add(CheckoutDto.builder().id(2L).build());

        ArrayList<CheckoutEntity> mappedEntityList = (ArrayList<CheckoutEntity>) mapper.convertToEntity(dtoList);
        String expected = dtoList.get(0).toString();
        String actual = mappedEntityList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapEntityListToDtoList() {
        ArrayList<CheckoutEntity> entityList = new ArrayList<>();
        entityList.add(CheckoutEntity.builder().id(1L).build());
        entityList.add(CheckoutEntity.builder().id(2L).build());

        ArrayList<CheckoutDto> mappedDtoList = (ArrayList<CheckoutDto>) mapper.convertToDto(entityList);
        String expected = entityList.get(0).toString();
        String actual = mappedDtoList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }
}
