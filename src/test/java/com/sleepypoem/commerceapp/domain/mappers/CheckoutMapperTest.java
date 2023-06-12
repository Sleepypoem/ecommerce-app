package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.entities.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutMapperTest {

    CheckoutMapper mapper = new CheckoutMapper();

    @Test
    @DisplayName("Testing if DTO is mapped to an entity")
    void testMappingDtoToEntity() {
        //arrange
        CheckoutDto checkoutDto = new CheckoutDto();
        checkoutDto.setId(1L);
        checkoutDto.setUserId("1");
        //act
        var result = mapper.convertToEntity(checkoutDto);
        //assert
        assertAll(
                () -> assertEquals(checkoutDto.getId(), result.getId()),
                () -> assertEquals(checkoutDto.getUserId(), result.getUserId())

        );
    }

    @Test
    @DisplayName("Testing if entity is mapped to a DTO")
    void testMappingEntityToDto() {
        //arrange
        CheckoutEntity checkoutEntity = new CheckoutEntity();
        checkoutEntity.setId(1L);
        checkoutEntity.setUserId("1");
        //act
        var result = mapper.convertToDto(checkoutEntity);
        //assert
        assertAll(
                () -> assertEquals(checkoutEntity.getId(), result.getId()),
                () -> assertEquals(checkoutEntity.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("Testing if a DTO list is mapped to a entity list")
    void testMappingDtoListToEntityList() {
        //arrange
        CheckoutDto checkoutDto1 = new CheckoutDto();
        checkoutDto1.setId(1L);

        CheckoutDto checkoutDto2 = new CheckoutDto();
        checkoutDto2.setId(2L);
        List<CheckoutDto> dtos = List.of(checkoutDto1, checkoutDto2);
        //act
        var result = mapper.convertToEntityList(dtos);
        //assert
        assertAll(
                () -> assertEquals(checkoutDto1.getId(), result.get(0).getId()),
                () -> assertEquals(checkoutDto2.getId(), result.get(1).getId())
        );
    }

    @Test
    @DisplayName("Testing if an entity list is mapped to a DTO list")
    void testMappingEntityListToDtoList() {
        //arrange
        CheckoutEntity checkoutEntity1 = new CheckoutEntity();
        checkoutEntity1.setId(1L);
        CheckoutEntity checkoutEntity2 = new CheckoutEntity();
        checkoutEntity2.setId(2L);
        List<CheckoutEntity> entities = List.of(checkoutEntity1, checkoutEntity2);
        //act
        var result = mapper.convertToDtoList(entities);
        //assert
        assertAll(
                () -> assertEquals(checkoutEntity1.getId(), result.get(0).getId()),
                () -> assertEquals(checkoutEntity2.getId(), result.get(1).getId())
        );
    }

}