package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CheckoutItemMapperTest {

    CheckoutItemMapper mapper = new CheckoutItemMapper();

    @Test
    @DisplayName("Testing if DTO is mapped to an entity")
    void testMappingDtoToEntity() {
        //arrange
        CheckoutItemDto checkoutItemDto = new CheckoutItemDto();
        checkoutItemDto.setId(1L);
        checkoutItemDto.setQuantity(1);
        //act
        var result = mapper.convertToEntity(checkoutItemDto);
        //assert
        assertAll(
                () -> assertEquals(checkoutItemDto.getId(), result.getId()),
                () -> assertEquals(checkoutItemDto.getQuantity(), result.getQuantity())
        );
    }

    @Test
    @DisplayName("Testing if entity is mapped to a DTO")
    void testMappingEntityToDto() {
        //arrange
        CheckoutItemEntity checkoutItemEntity = new CheckoutItemEntity();
        checkoutItemEntity.setId(1L);
        checkoutItemEntity.setQuantity(1);
        //act
        var result = mapper.convertToDto(checkoutItemEntity);
        //assert
        assertAll(
                () -> assertEquals(checkoutItemEntity.getId(), result.getId()),
                () -> assertEquals(checkoutItemEntity.getQuantity(), result.getQuantity())
        );
    }

    @Test
    @DisplayName("Testing if a DTO list is mapped to a entity list")
    void testMappingDtoListToEntityList() {
        //arrange
        CheckoutItemDto checkoutItemDto1 = new CheckoutItemDto();
        checkoutItemDto1.setId(1L);
        checkoutItemDto1.setQuantity(1);
        CheckoutItemDto checkoutItemDto2 = new CheckoutItemDto();
        checkoutItemDto2.setId(2L);
        checkoutItemDto2.setQuantity(2);
        List<CheckoutItemDto> dtos = List.of(checkoutItemDto1, checkoutItemDto2);
        //act
        var result = mapper.convertToEntityList(dtos);
        //assert
        assertAll(
                () -> assertEquals(checkoutItemDto1.getId(), result.get(0).getId()),
                () -> assertEquals(checkoutItemDto1.getQuantity(), result.get(0).getQuantity()),
                () -> assertEquals(checkoutItemDto2.getId(), result.get(1).getId()),
                () -> assertEquals(checkoutItemDto2.getQuantity(), result.get(1).getQuantity())
        );
    }

    @Test
    @DisplayName("Testing if an entity list is mapped to a DTO list")
    void testMappingEntityListToDtoList() {
        //arrange
        CheckoutItemEntity checkoutItemEntity1 = new CheckoutItemEntity();
        checkoutItemEntity1.setId(1L);
        checkoutItemEntity1.setQuantity(1);
        CheckoutItemEntity checkoutItemEntity2 = new CheckoutItemEntity();
        checkoutItemEntity2.setId(2L);
        checkoutItemEntity2.setQuantity(2);
        List<CheckoutItemEntity> entities = List.of(checkoutItemEntity1, checkoutItemEntity2);
        //act
        var result = mapper.convertToDtoList(entities);
        //assert
        assertAll(
                () -> assertEquals(checkoutItemEntity1.getId(), result.get(0).getId()),
                () -> assertEquals(checkoutItemEntity1.getQuantity(), result.get(0).getQuantity()),
                () -> assertEquals(checkoutItemEntity2.getId(), result.get(1).getId()),
                () -> assertEquals(checkoutItemEntity2.getQuantity(), result.get(1).getQuantity())
        );
    }


}