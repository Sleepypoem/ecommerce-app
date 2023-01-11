package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentMethodMapperTest {
    PaymentMethodMapper mapper;

    @BeforeEach
    void init() {
        mapper = new PaymentMethodMapper();
    }

    @Test
    void testMethodReturnsEntityInstance() {
        PaymentMethodEntity entity = mapper.getEntityInstance();
        assertNotNull(entity);
    }

    @Test
    void testMethodReturnsDtoInstance() {
        PaymentMethodDto dto = mapper.getDtoInstance();
        assertNotNull(dto);
    }

    @Test
    void testMapEntityToDto() {
        PaymentMethodEntity entity = PaymentMethodEntity.
                builder()
                .id(1L)
                .build();

        PaymentMethodDto mappedDto = mapper.convertToDto(entity);
        Long expected = entity.getId();
        Long actual = mappedDto.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoToEntity() {
        PaymentMethodDto dto = PaymentMethodDto
                .builder()
                .id(1L)
                .build();

        PaymentMethodEntity mappedEntity = mapper.convertToEntity(dto);
        Long expected = dto.getId();
        Long actual = mappedEntity.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoListToEntityList() {
        ArrayList<PaymentMethodDto> dtoList = new ArrayList<>();
        dtoList.add(PaymentMethodDto.builder().id(1L).build());
        dtoList.add(PaymentMethodDto.builder().id(2L).build());

        ArrayList<PaymentMethodEntity> mappedEntityList = (ArrayList<PaymentMethodEntity>) mapper.convertToEntity(dtoList);
        String expected = dtoList.get(0).toString();
        String actual = mappedEntityList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapEntityListToDtoList() {
        ArrayList<PaymentMethodEntity> entityList = new ArrayList<>();
        entityList.add(PaymentMethodEntity.builder().id(1L).build());
        entityList.add(PaymentMethodEntity.builder().id(2L).build());

        ArrayList<PaymentMethodDto> mappedDtoList = (ArrayList<PaymentMethodDto>) mapper.convertToDto(entityList);
        String expected = entityList.get(0).toString();
        String actual = mappedDtoList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }
}
