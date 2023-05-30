package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PaymentMethodMapperTest {

    PaymentMethodMapper mapper = new PaymentMethodMapper();

    @Test
    @DisplayName("Testing if DTO is mapped to an entity")
    void testMappingDtoToEntity() {
        //arrange
        PaymentMethodDto paymentMethodDto = new PaymentMethodDto();
        paymentMethodDto.setId(1L);
        paymentMethodDto.setUserId("User id");
        //act
        var result = mapper.convertToEntity(paymentMethodDto);
        //assert
        assertAll(
                () -> assertEquals(paymentMethodDto.getId(), result.getId()),
                () -> assertEquals(paymentMethodDto.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("Testing if entity is mapped to a DTO")
    void testMappingEntityToDto() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodEntity();
        paymentMethodEntity.setId(1L);
        paymentMethodEntity.setUserId("User id");
        //act
        var result = mapper.convertToDto(paymentMethodEntity);
        //assert
        assertAll(
                () -> assertEquals(paymentMethodEntity.getId(), result.getId()),
                () -> assertEquals(paymentMethodEntity.getUserId(), result.getUserId())
        );
    }

    @Test
    @DisplayName("Testing if a DTO list is mapped to a entity list")
    void testMappingDtoListToEntityList() {
        //arrange
        PaymentMethodDto paymentMethodDto1 = new PaymentMethodDto();
        paymentMethodDto1.setId(1L);

        PaymentMethodDto paymentMethodDto2 = new PaymentMethodDto();
        paymentMethodDto2.setId(2L);
        List<PaymentMethodDto> dtos = List.of(paymentMethodDto1, paymentMethodDto2);
        //act
        var result = mapper.convertToEntityList(dtos);
        //assert
        assertAll(
                () -> assertEquals(paymentMethodDto1.getId(), result.get(0).getId()),
                () -> assertEquals(paymentMethodDto2.getId(), result.get(1).getId())
        );
    }

    @Test
    @DisplayName("Testing if an entity list is mapped to a DTO list")
    void testMappingEntityListToDtoList() {
        //arrange
        PaymentMethodEntity paymentMethodEntity1 = new PaymentMethodEntity();
        paymentMethodEntity1.setId(1L);
        PaymentMethodEntity paymentMethodEntity2 = new PaymentMethodEntity();
        paymentMethodEntity2.setId(2L);
        List<PaymentMethodEntity> entities = List.of(paymentMethodEntity1, paymentMethodEntity2);
        //act
        var result = mapper.convertToDtoList(entities);
        //assert
        assertAll(
                () -> assertEquals(paymentMethodEntity1.getId(), result.get(0).getId()),
                () -> assertEquals(paymentMethodEntity2.getId(), result.get(1).getId())
        );
    }

}