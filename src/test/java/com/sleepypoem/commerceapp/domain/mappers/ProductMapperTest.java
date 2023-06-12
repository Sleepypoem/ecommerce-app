package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.entities.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductMapperTest {

    ProductMapper mapper = new ProductMapper();

    @Test
    @DisplayName("Testing if DTO is mapped to an entity")
    void testMappingDtoToEntity() {
        //arrange
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setDescription("test");
        productDto.setPrice(1.0);
        productDto.setStock(1);
        //act
        var result = mapper.convertToEntity(productDto);
        //assert
        assertAll(
                () -> assertEquals(productDto.getId(), result.getId()),
                () -> assertEquals(productDto.getDescription(), result.getDescription()),
                () -> assertEquals(productDto.getPrice(), result.getPrice()),
                () -> assertEquals(productDto.getStock(), result.getStock())
        );
    }

    @Test
    @DisplayName("Testing if entity is mapped to a DTO")
    void testMappingEntityToDto() {
        //arrange
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setId(1L);
        productEntity.setDescription("test");
        productEntity.setPrice(1.0);
        productEntity.setStock(1);
        //act
        var result = mapper.convertToDto(productEntity);
        //assert
        assertAll(
                () -> assertEquals(productEntity.getId(), result.getId()),
                () -> assertEquals(productEntity.getDescription(), result.getDescription()),
                () -> assertEquals(productEntity.getPrice(), result.getPrice()),
                () -> assertEquals(productEntity.getStock(), result.getStock())
        );
    }

    @Test
    @DisplayName("Testing if a DTO list is mapped to a entity list")
    void testMappingDtoListToEntityList() {
        //arrange
        ProductDto productDto1 = new ProductDto();
        productDto1.setId(1L);

        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);
        List<ProductDto> dtos = List.of(productDto1, productDto2);
        //act
        var result = mapper.convertToEntityList(dtos);
        //assert
        assertAll(
                () -> assertEquals(productDto1.getId(), result.get(0).getId()),
                () -> assertEquals(productDto2.getId(), result.get(1).getId())
        );
    }

    @Test
    @DisplayName("Testing if an entity list is mapped to a DTO list")
    void testMappingEntityListToDtoList() {
        //arrange
        ProductEntity productEntity1 = new ProductEntity();
        productEntity1.setId(1L);
        ProductEntity productEntity2 = new ProductEntity();
        productEntity2.setId(2L);
        List<ProductEntity> entities = List.of(productEntity1, productEntity2);
        //act
        var result = mapper.convertToDtoList(entities);
        //assert
        assertAll(
                () -> assertEquals(productEntity1.getId(), result.get(0).getId()),
                () -> assertEquals(productEntity2.getId(), result.get(1).getId())
        );
    }

}