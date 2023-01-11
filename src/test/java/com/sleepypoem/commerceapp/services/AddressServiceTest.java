package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.AddressMapper;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;
import com.sleepypoem.commerceapp.repositories.AddressRepository;
import com.sleepypoem.commerceapp.services.validators.IValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    @Mock
    private AddressRepository repository;

    @Mock
    private IValidator<AddressEntity> validator;

    @Mock
    private AddressMapper mapper;

    @InjectMocks
    private AddressService service;

    @BeforeEach
    void init() {
        service = new AddressService(validator, repository, mapper);
    }

    @Test
    void testCreateEntity() throws Exception {
        AddressEntity entity = AddressEntity
                .builder()
                .id(1L)
                .build();
        when(validator.isValid(entity)).thenReturn(true);
        when(repository.save(any(AddressEntity.class))).thenReturn(entity);
        when(mapper.convertToDto(entity)).thenReturn(AddressDto.builder().id(1L).build());
        AddressDto persisted = service.create(entity);
        assertEquals(entity.toString(), persisted.toString());
        verify(repository).save(entity);
        verify(validator).isValid(entity);
        verify(mapper).convertToDto(entity);
    }

    @Test
    void testUpdateEntity() throws Exception {
        AddressEntity entity = AddressEntity
                .builder()
                .id(1L)
                .country("US")
                .build();

        AddressEntity newEntity = AddressEntity
                .builder()
                .id(1L)
                .country("CAN")
                .build();

        when(validator.isValid(any(AddressEntity.class))).thenReturn(true).thenReturn(true);
        when(repository.save(any(AddressEntity.class))).thenReturn(entity).thenReturn(newEntity);
        when(mapper.convertToDto(any(AddressEntity.class)))
                .thenReturn(AddressDto.builder().id(1L).country("US").build())
                .thenReturn(AddressDto.builder().id(1L).country("CAN").build());
        service.create(entity);
        AddressDto updatedEntity = service.update(1L, newEntity);
        assertEquals("CAN", updatedEntity.getCountry());
        verify(validator).isValid(entity);
        verify(validator).isValid(newEntity);
        verify(repository).save(entity);
        verify(repository).save(newEntity);
        verify(mapper).convertToDto(entity);
        verify(mapper).convertToDto(newEntity);
    }

    @Test
    void testDeleteEntity() throws Exception {
        AddressEntity entity = AddressEntity
                .builder()
                .id(1L)
                .build();

        when(validator.isValid(entity)).thenReturn(true);
        when(repository.save(any(AddressEntity.class))).thenReturn(entity);
        when(mapper.convertToDto(entity)).thenReturn(AddressDto.builder().id(1L).build());
        service.create(entity);
        boolean expected = service.delete(1L);
        assertTrue(expected);
        verify(validator).isValid(entity);
        verify(repository).save(entity);
        verify(mapper).convertToDto(entity);
        verify(repository).deleteById(1L);
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void testGetEntityById() {
        AddressEntity entity = AddressEntity
                .builder()
                .id(1L)
                .state("Arkansas")
                .build();

        when(mapper.convertToDto(entity))
                .thenReturn(AddressDto.builder().id(1L).state("Arkansas").build());
        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        AddressDto searchedEntity = service.getOneById(1L).get();

        assertEquals(entity.toString(), searchedEntity.toString());

        verify(mapper).convertToDto(entity);
        verify(repository).findById(1L);
    }

    @Test
    void testGetAllEntities() {
        AddressEntity entity = AddressEntity
                .builder()
                .id(1L)
                .state("Arkansas")
                .build();

        List<AddressEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        AddressDto dto = AddressDto
                .builder()
                .id(1L)
                .state("Arkansas")
                .build();

        List<AddressDto> dtoList = new ArrayList<>();
        dtoList.add(dto);

        when(repository.findAll()).thenReturn(entityList);
        when(mapper.convertToDtoList(anyList())).thenReturn(dtoList);

        List<AddressDto> expected = service.getAll();

        assertArrayEquals(dtoList.toArray(), expected.toArray());

        verify(repository).findAll();
        verify(mapper).convertToDtoList(entityList);
    }

    @Test
    void testGetByUserId() {
        AddressEntity entity = AddressEntity
                .builder()
                .id(1L)
                .state("Arkansas")
                .build();

        List<AddressEntity> entityList = new ArrayList<>();
        entityList.add(entity);

        when(repository.findByUserId(anyString())).thenReturn(entityList);

        List<AddressEntity> expected = service.findByUserId("Test");

        assertArrayEquals(entityList.toArray(), expected.toArray());

        verify(repository).findByUserId("Test");

    }

    @Test
    void testExceptionWhenEntityNotFound() {

        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(MyEntityNotFoundException.class, () -> {
            service.getOneById(1L);
        });

        verify(repository).findById(1L);
    }

    @Test
    void testExceptionWhenValidationFails() throws Exception {
        AddressEntity entity = AddressEntity
                .builder()
                .id(1L)
                .build();

        when(validator.isValid(entity)).thenReturn(false);
        assertThrows(MyValidationException.class, () -> {
            service.create(entity);
        });
        verify(validator).isValid(entity);
    }

}