package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AddressMapperTest {

    AddressMapper mapper;

    @BeforeEach
    void init() {
        mapper = new AddressMapper();
    }

    @Test
    void testMethodReturnsEntityInstance() {
        AddressEntity entity = mapper.getEntityInstance();
        assertNotNull(entity);
    }

    @Test
    void testMethodReturnsDtoInstance() {
        AddressDto dto = mapper.getDtoInstance();
        assertNotNull(dto);
    }

    @Test
    void testMapEntityToDto() {
        AddressEntity entity = AddressEntity.
                builder()
                .id(1L)
                .userId("test")
                .country("US")
                .state("testState")
                .zipCode("12345")
                .firstLine("test")
                .secondLine("test2")
                .build();

        AddressDto mappedDto = mapper.convertToDto(entity);
        String expected = entity.getUserId();
        String actual = mappedDto.getUserId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoToEntity() {
        AddressDto dto = AddressDto
                .builder()
                .id(1L)
                .userId("test")
                .country("US")
                .state("testState")
                .zipCode("12345")
                .firstLine("test")
                .secondLine("test2")
                .build();

        AddressEntity mappedEntity = mapper.convertToEntity(dto);
        String expected = dto.getUserId();
        String actual = mappedEntity.getUserId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoListToEntityList() {
        ArrayList<AddressDto> dtoList = new ArrayList<>();
        dtoList.add(AddressDto.builder().id(1L).build());
        dtoList.add(AddressDto.builder().id(2L).build());

        ArrayList<AddressEntity> mappedEntityList = (ArrayList<AddressEntity>) mapper.convertToEntity(dtoList);
        String expected = dtoList.get(0).toString();
        String actual = mappedEntityList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapEntityListToDtoList() {
        ArrayList<AddressEntity> entityList = new ArrayList<>();
        entityList.add(AddressEntity.builder().id(1L).build());
        entityList.add(AddressEntity.builder().id(2L).build());

        ArrayList<AddressDto> mappedDtoList = (ArrayList<AddressDto>) mapper.convertToDto(entityList);
        String expected = entityList.get(0).toString();
        String actual = mappedDtoList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }
}