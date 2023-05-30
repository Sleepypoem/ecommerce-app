package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AddressMapperTest {

    AddressMapper mapper = new AddressMapper();

    @Test
    @DisplayName("Testing if DTO is mapped to an entity")
    void testMappingDtoToEntity() {
        //arrange
        AddressDto addressDto = new AddressDto();
        addressDto.setId(1L);
        addressDto.setCountry("Poland");
        addressDto.setFirstLine("First line");
        addressDto.setSecondLine("Second line");
        addressDto.setState("State");
        addressDto.setZipCode("Zip code");
        addressDto.setUserId("User id");
        //act
        var result = mapper.convertToEntity(addressDto);
        //assert
        assertAll(
                () -> assertEquals(addressDto.getId(), result.getId()),
                () -> assertEquals(addressDto.getCountry(), result.getCountry()),
                () -> assertEquals(addressDto.getFirstLine(), result.getFirstLine()),
                () -> assertEquals(addressDto.getSecondLine(), result.getSecondLine()),
                () -> assertEquals(addressDto.getState(), result.getState()),
                () -> assertEquals(addressDto.getZipCode(), result.getZipCode()),
                () -> assertEquals(addressDto.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("Testing if entity is mapped to a DTO")
    void testMappingEntityToDto() {
        //arrange
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setCountry("Poland");
        addressEntity.setFirstLine("First line");
        addressEntity.setSecondLine("Second line");
        addressEntity.setState("State");
        addressEntity.setZipCode("Zip code");
        addressEntity.setUserId("User id");
        //act
        var result = mapper.convertToDto(addressEntity);
        //assert
        assertAll(
                () -> assertEquals(addressEntity.getId(), result.getId()),
                () -> assertEquals(addressEntity.getCountry(), result.getCountry()),
                () -> assertEquals(addressEntity.getFirstLine(), result.getFirstLine()),
                () -> assertEquals(addressEntity.getSecondLine(), result.getSecondLine()),
                () -> assertEquals(addressEntity.getState(), result.getState()),
                () -> assertEquals(addressEntity.getZipCode(), result.getZipCode()),
                () -> assertEquals(addressEntity.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("Testing if a DTO list is mapped to a entity list")
    void testMappingDtoListToEntityList() {
        //arrange
        AddressDto addressDto1 = new AddressDto();
        addressDto1.setId(1L);

        AddressDto addressDto2 = new AddressDto();
        addressDto2.setId(2L);
        List<AddressDto> dtos = List.of(addressDto1, addressDto2);
        //act
        var result = mapper.convertToEntityList(dtos);
        //assert
        assertAll(
                () -> assertEquals(addressDto1.getId(), result.get(0).getId()),
                () -> assertEquals(addressDto2.getId(), result.get(1).getId())
        );
    }

    @Test
    @DisplayName("Testing if an entity list is mapped to a DTO list")
    void testMappingEntityListToDtoList() {
        //arrange
        AddressEntity addressEntity1 = new AddressEntity();
        addressEntity1.setId(1L);
        AddressEntity addressEntity2 = new AddressEntity();
        addressEntity2.setId(2L);
        List<AddressEntity> entities = List.of(addressEntity1, addressEntity2);
        //act
        var result = mapper.convertToDtoList(entities);
        //assert
        assertAll(
                () -> assertEquals(addressEntity1.getId(), result.get(0).getId()),
                () -> assertEquals(addressEntity2.getId(), result.get(1).getId())
        );
    }

}