package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.domain.dto.PaymentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PaymentMapperTest {
    PaymentMapper mapper;

    @BeforeEach
    void init() {
        mapper = new PaymentMapper();
    }

    @Test
    void testMethodReturnsEntityInstance() {
        PaymentEntity entity = mapper.getEntityInstance();
        assertNotNull(entity);
    }

    @Test
    void testMethodReturnsDtoInstance() {
        PaymentDto dto = mapper.getDtoInstance();
        assertNotNull(dto);
    }

    @Test
    void testMapEntityToDto() {
        PaymentEntity entity = PaymentEntity.
                builder()
                .id(1L)
                .build();

        PaymentDto mappedDto = mapper.convertToDto(entity);
        Long expected = entity.getId();
        Long actual = mappedDto.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoToEntity() {
        PaymentDto dto = PaymentDto
                .builder()
                .id(1L)
                .build();

        PaymentEntity mappedEntity = mapper.convertToEntity(dto);
        Long expected = dto.getId();
        Long actual = mappedEntity.getId();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapDtoListToEntityList() {
        ArrayList<PaymentDto> dtoList = new ArrayList<>();
        dtoList.add(PaymentDto.builder().id(1L).build());
        dtoList.add(PaymentDto.builder().id(2L).build());

        ArrayList<PaymentEntity> mappedEntityList = (ArrayList<PaymentEntity>) mapper.convertToEntity(dtoList);
        String expected = dtoList.get(0).toString();
        String actual = mappedEntityList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }

    @Test
    void testMapEntityListToDtoList() {
        ArrayList<PaymentEntity> entityList = new ArrayList<>();
        entityList.add(PaymentEntity.builder().id(1L).build());
        entityList.add(PaymentEntity.builder().id(2L).build());

        ArrayList<PaymentDto> mappedDtoList = (ArrayList<PaymentDto>) mapper.convertToDto(entityList);
        String expected = entityList.get(0).toString();
        String actual = mappedDtoList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
    }
}
