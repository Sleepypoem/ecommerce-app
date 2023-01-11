package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.ReceiptDto;
import com.sleepypoem.commerceapp.domain.entities.ReceiptEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReceiptMapperTest {


    ReceiptMapper mapper;

    @BeforeEach
    void init() {
        mapper = new ReceiptMapper();
    }

    @Test
    void testMethodReturnsEntityInstance() {
        ReceiptEntity entity = mapper.getEntityInstance();
        assertNotNull(entity);
    }

    @Test
    void testMethodReturnsDtoInstance() {
        ReceiptDto dto = mapper.getDtoInstance();
        assertNotNull(dto);
    }

    @Test
    void testMapEntityToDto() {
        ReceiptEntity entity = ReceiptEntity.
                builder()
                .id(1L)
                .build();

        ReceiptDto mappedDto = mapper.convertToDto(entity);
        Long expected = entity.getId();
        Long actual = mappedDto.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoToEntity() {
        ReceiptDto dto = ReceiptDto
                .builder()
                .id(1L)
                .build();

        ReceiptEntity mappedEntity = mapper.convertToEntity(dto);
        Long expected = dto.getId();
        Long actual = mappedEntity.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoListToEntityList() {
        ArrayList<ReceiptDto> dtoList = new ArrayList<>();
        dtoList.add(ReceiptDto.builder().id(1L).build());
        dtoList.add(ReceiptDto.builder().id(2L).build());

        ArrayList<ReceiptEntity> mappedEntityList = (ArrayList<ReceiptEntity>) mapper.convertToEntity(dtoList);
        String expected = dtoList.get(0).toString();
        String actual = mappedEntityList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapEntityListToDtoList() {
        ArrayList<ReceiptEntity> entityList = new ArrayList<>();
        entityList.add(ReceiptEntity.builder().id(1L).build());
        entityList.add(ReceiptEntity.builder().id(2L).build());

        ArrayList<ReceiptDto> mappedDtoList = (ArrayList<ReceiptDto>) mapper.convertToDto(entityList);
        String expected = entityList.get(0).toString();
        String actual = mappedDtoList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }
}
