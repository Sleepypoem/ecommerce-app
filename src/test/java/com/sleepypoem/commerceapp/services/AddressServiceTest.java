package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.AddressRepository;
import com.sleepypoem.commerceapp.utils.factories.impl.AddressFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.sleepypoem.commerceapp.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    AddressService service;

    @Mock
    AddressRepository repository;

    AddressFactory factory;

    @BeforeEach
    void setUp() {

        service = new AddressService(repository);
        factory = new AddressFactory();
    }

    @Test
    @DisplayName("Test finding an address by id")
    void testFindAddressById() {
        //arrange
        AddressEntity address = factory.create();
        long id = address.getId();
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.of(address));
        //act
        AddressEntity foundAddress = service.getOneById(id);
        //assert
        assertThat(foundAddress, equalTo(address));
        verify(repository).findById(id);
    }

    @Test
    @DisplayName("Test finding an address by id that does not exist")
    void testFindAddressByIdWhenAddressNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.getOneById(1L));
        assertThat(ex.getMessage(), is("Address with id 1 not found"));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test creating an address")
    void testCreateAddressWhenOk() {
        //arrange
        AddressEntity address = factory.create();
        when(repository.save(any(AddressEntity.class))).thenReturn(address);
        //act
        AddressEntity createdAddress = service.create(address);
        //assert
        assertThat(address, equalTo(createdAddress));
        verify(repository).save(address);
    }

    @Test
    @DisplayName("Test updating an address")
    void testUpdateAddressWhenOk() {
        //arrange
        AddressEntity address = factory.create();
        long id = address.getId();
        when(repository.save(any(AddressEntity.class))).thenReturn(address);
        //act
        AddressEntity updatedAddress = service.update(id, address);
        //assert
        assertThat(address, is(updatedAddress));
        verify(repository).save(address);
    }

    @Test
    @DisplayName("Test updating an address when id in path and id in body do not match")
    void testUpdateAddressWhenIdMismatch() {
        //arrange
        AddressEntity address = factory.create();
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> service.update(1L, address));
        assertThat(ex.getMessage(), is("Id in URI doesn't match with entity id."));
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Test deleting an address")
    void testDeleteAddress() {
        //arrange
        AddressEntity address = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(address));
        //act
        boolean result = service.deleteById(1L);
        //assert
        assertThat(result, is(true));
        verify(repository).findById(1L);
        verify(repository).delete(address);
    }

    @Test
    @DisplayName("Test deleting an address when delete throws an exception")
    void testDeleteAddressWhenDeleteThrowsException() {
        //arrange
        var address = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(address));
        doThrow(new RuntimeException("")).when(repository).delete(address);
        //act
        boolean result = service.deleteById(1L);
        //assert
        assertThat(result, is(false));
        verify(repository).findById(1L);
        verify(repository).delete(address);
    }

    @Test
    @DisplayName("Test deleting an address that does not exist")
    void testDeleteAddressThatDoesNotExist() {
        //arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.deleteById(1L));
        assertThat(ex.getMessage(), is("Address with id 1 not found"));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding all addresses")
    void testFindAllAddressesWhenOk() {
        //arrange
        List<AddressEntity> addresses = factory.createList(50);
        when(repository.findAll()).thenReturn(addresses);
        //act
        List<AddressEntity> result = service.getAll();
        //assert
        assertThat(result, equalTo(addresses));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test finding all addresses when there are none")
    void testFindAllAddressesWhenThereAreNone() {
        //arrange
        when(repository.findAll()).thenReturn(List.of());
        //act
        List<AddressEntity> addresses = service.getAll();
        //assert
        assertThat(addresses, is(empty()));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test getting a list of Addresses by user id")
    void testGetAddressesByUserIdWhenOk() {
        //arrange
        List<AddressEntity> addresses = factory.createList(50);
        when(repository.findByUserId(eq("testUser"), any(Pageable.class))).thenReturn(
                new PageImpl<>(addresses, DEFAULT_PAGEABLE, DEFAULT_TOTAL_ELEMENTS)
        );
        //act
        Page<AddressEntity> result = service.findByUserId("testUser", DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(addresses, result.getContent()),
                () -> assertEquals(DEFAULT_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, result.getTotalElements()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getSort().getOrderFor("id").getDirection().name())
        );
        verify(repository).findByUserId("testUser", DEFAULT_PAGEABLE);
    }

    @Test
    @DisplayName("Test getting a list of Addresses paginated and sorted")
    void testGetAddressesPaginatedAndSortedWhenOk() {
        //arrange
        var addresses = factory.createList(50);
        when(repository.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(addresses, DEFAULT_PAGEABLE, DEFAULT_TOTAL_ELEMENTS)
        );
        //act
        var result = service.getAllPaginatedAndSorted(DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(addresses, result.getContent()),
                () -> assertEquals(DEFAULT_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, result.getTotalElements()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getSort().getOrderFor("id").getDirection().name())
        );
        verify(repository).findAll(DEFAULT_PAGEABLE);
    }

    @Test
    @DisplayName("Test getting a list of Addresses by user id when user not found or has no addresses")
    void testGetAddressesByUserIdWhenUserNotFoundOrHasNoAddresses() {
        //arrange
        when(repository.findByUserId(anyString(), any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(), DEFAULT_PAGEABLE, ZERO_TOTAL_ELEMENTS)
        );
        //act
        Page<AddressEntity> result = service.getAllPaginatedAndSortedByUserId("userId", DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(ZERO_TOTAL_ELEMENTS, result.getTotalElements()),
                () -> assertEquals(0, result.getTotalPages()),
                () -> assertEquals(DEFAULT_SIZE, result.getSize()),
                () -> assertEquals(DEFAULT_PAGE, result.getNumber()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getSort().getOrderFor("id").getDirection().name()),
                () -> assertEquals(List.of(), result.getContent()));
        verify(repository).findByUserId("userId", DEFAULT_PAGEABLE);
    }
}