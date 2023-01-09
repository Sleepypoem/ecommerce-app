package com.sleepypoem.commerceapp.domain.mappers;

import com.sleepypoem.commerceapp.utils.TestDto;
import com.sleepypoem.commerceapp.utils.TestEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleMapperTest {

    @Mock(answer = Answers.CALLS_REAL_METHODS)
    SimpleMapper<TestDto, TestEntity> mapper;

    @Test
    void testMapEntityToDto() {
        when(mapper.getDtoInstance()).thenReturn(new TestDto());
        TestEntity entity = new TestEntity(1L, "test", 1);
        TestDto mappedDto = mapper.convertToDto(entity);
        String expected = entity.getProperty1();
        String actual = mappedDto.getProperty1();
        assertThat(actual, is(equalTo(expected)));
        verify(mapper).getDtoInstance();
    }

    @Test
    void testMapDtoToEntity() {
        when(mapper.getEntityInstance()).thenReturn(new TestEntity());
        TestDto dto = new TestDto(1L, "test", 1);
        TestEntity mappedEntity = mapper.convertToEntity(dto);
        String expected = dto.getProperty1();
        String actual = mappedEntity.getProperty1();
        assertThat(actual, is(equalTo(expected)));
        verify(mapper).getEntityInstance();
    }

    @Test
    void testMapDtoListToEntityList() {
        when(mapper.getEntityInstance()).thenReturn(new TestEntity());
        ArrayList<TestDto> dtoList = new ArrayList<>();
        dtoList.add(new TestDto(1L, "test1", 1));
        dtoList.add(new TestDto(2L, "test2", 2));

        ArrayList<TestEntity> mappedEntityList = (ArrayList<TestEntity>) mapper.convertToEntity(dtoList);
        /*System.out.println(mappedEntityList.get(0).toString());
        System.out.println(mappedEntityList.get(1).toString());
        String expected = dtoList.get(0).toString();
        String actual = mappedEntityList.get(0).toString();
        assertThat(actual, is(equalTo(expected)));
        verify(mapper).getEntityInstance();*/
    }

}