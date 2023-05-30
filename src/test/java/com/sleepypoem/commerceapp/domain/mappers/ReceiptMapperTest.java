package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.ReceiptDto;
import com.sleepypoem.commerceapp.domain.entities.ReceiptEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReceiptMapperTest {

    ReceiptMapper mapper = new ReceiptMapper();

    @Test
    @DisplayName("Testing if DTO is mapped to an entity")
    void testMappingDtoToEntity() {
        //arrange
        ReceiptDto receiptDto = new ReceiptDto();
        receiptDto.setId(1L);
        receiptDto.setCurrencyType("USD");
        receiptDto.setTotal(100.0);
        receiptDto.setUserFirstName("test");
        receiptDto.setUserLastName("test");
        //act
        var result = mapper.convertToEntity(receiptDto);
        //assert
        assertAll(
                () -> assertEquals(receiptDto.getId(), result.getId()),
                () -> assertEquals(receiptDto.getCurrencyType(), result.getCurrencyType()),
                () -> assertEquals(receiptDto.getTotal(), result.getTotal()),
                () -> assertEquals(receiptDto.getUserFirstName(), result.getUserFirstName()),
                () -> assertEquals(receiptDto.getUserLastName(), result.getUserLastName())
        );
    }

    @Test
    @DisplayName("Testing if entity is mapped to a DTO")
    void testMappingEntityToDto() {
        //arrange
        ReceiptEntity receiptEntity = new ReceiptEntity();
        receiptEntity.setId(1L);
        //act
        var result = mapper.convertToDto(receiptEntity);
        //assert
        assertAll(
                () -> assertEquals(receiptEntity.getId(), result.getId()),
                () -> assertEquals(receiptEntity.getCurrencyType(), result.getCurrencyType()),
                () -> assertEquals(receiptEntity.getTotal(), result.getTotal()),
                () -> assertEquals(receiptEntity.getUserFirstName(), result.getUserFirstName()),
                () -> assertEquals(receiptEntity.getUserLastName(), result.getUserLastName())
        );
    }

    @Test
    @DisplayName("Testing if a DTO list is mapped to a entity list")
    void testMappingDtoListToEntityList() {
        //arrange
        ReceiptDto receiptDto1 = new ReceiptDto();
        receiptDto1.setId(1L);

        ReceiptDto receiptDto2 = new ReceiptDto();
        receiptDto2.setId(2L);
        List<ReceiptDto> dtos = List.of(receiptDto1, receiptDto2);
        //act
        var result = mapper.convertToEntityList(dtos);
        //assert
        assertAll(
                () -> assertEquals(receiptDto1.getId(), result.get(0).getId()),
                () -> assertEquals(receiptDto2.getId(), result.get(1).getId())
        );
    }

    @Test
    @DisplayName("Testing if an entity list is mapped to a DTO list")
    void testMappingEntityListToDtoList() {
        //arrange
        ReceiptEntity receiptEntity1 = new ReceiptEntity();
        receiptEntity1.setId(1L);
        ReceiptEntity receiptEntity2 = new ReceiptEntity();
        receiptEntity2.setId(2L);
        List<ReceiptEntity> entities = List.of(receiptEntity1, receiptEntity2);
        //act
        var result = mapper.convertToDtoList(entities);
        //assert
        assertAll(
                () -> assertEquals(receiptEntity1.getId(), result.get(0).getId()),
                () -> assertEquals(receiptEntity2.getId(), result.get(1).getId())
        );
    }

}