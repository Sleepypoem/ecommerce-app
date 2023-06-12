package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.entities.PaymentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentMapperTest {

    PaymentMapper mapper = new PaymentMapper();

    @Test
    @DisplayName("Testing if DTO is mapped to an entity")
    void testMappingDtoToEntity() {
        //arrange
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(1L);
        paymentDto.setUserId("User id");
        //act
        var result = mapper.convertToEntity(paymentDto);
        //assert
        assertAll(
                () -> assertEquals(paymentDto.getId(), result.getId()),
                () -> assertEquals(paymentDto.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("Testing if entity is mapped to a DTO")
    void testMappingEntityToDto() {
        //arrange
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setId(1L);
        paymentEntity.setUserId("User id");
        //act
        var result = mapper.convertToDto(paymentEntity);
        //assert
        assertAll(
                () -> assertEquals(paymentEntity.getId(), result.getId()),
                () -> assertEquals(paymentEntity.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("Testing if a DTO list is mapped to a entity list")
    void testMappingDtoListToEntityList() {
        //arrange
        PaymentDto paymentDto1 = new PaymentDto();
        paymentDto1.setId(1L);

        PaymentDto paymentDto2 = new PaymentDto();
        paymentDto2.setId(2L);
        List<PaymentDto> dtos = List.of(paymentDto1, paymentDto2);
        //act
        var result = mapper.convertToEntityList(dtos);
        //assert
        assertAll(
                () -> assertEquals(paymentDto1.getId(), result.get(0).getId()),
                () -> assertEquals(paymentDto2.getId(), result.get(1).getId())
        );
    }

    @Test
    @DisplayName("Testing if an entity list is mapped to a DTO list")
    void testMappingEntityListToDtoList() {
        //arrange
        PaymentEntity paymentEntity1 = new PaymentEntity();
        paymentEntity1.setId(1L);
        PaymentEntity paymentEntity2 = new PaymentEntity();
        paymentEntity2.setId(2L);
        List<PaymentEntity> entities = List.of(paymentEntity1, paymentEntity2);
        //act
        var result = mapper.convertToDtoList(entities);
        //assert
        assertAll(
                () -> assertEquals(paymentEntity1.getId(), result.get(0).getId()),
                () -> assertEquals(paymentEntity2.getId(), result.get(1).getId())
        );
    }

}