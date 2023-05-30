package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.AddressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    AddressService addressService;

    @Mock
    AddressRepository repository;

    @BeforeEach
    void setUp() {
        addressService = new AddressService(repository);
    }

    @Test
    @DisplayName("Test finding address by user id")
    @Disabled
    void testFindAddressByUserId() {
        //arrange
        AddressEntity address = new AddressEntity();
        address.setUserId("testUser");
        address.setId(1L);
        PageImpl<AddressEntity> addressPage = new PageImpl<>(List.of(address));
        when(repository.findByUserId(eq("testUser"), any(Pageable.class))).thenReturn(addressPage);
        //act
        Page<AddressEntity> addresses = addressService.findByUserId("testUser", 1, 10, "id", "asc");
        //assert
        assertEquals(address, addresses.getContent().get(0));
        verify(repository).findByUserId(eq("testUser"), any(Pageable.class));
    }

    @Test
    @DisplayName("Test finding an address by id")
    void testFindAddressById() {
        //arrange
        AddressEntity address = new AddressEntity();
        address.setId(1L);
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(address));
        //act
        AddressEntity foundAddress = addressService.getOneById(1L);
        //assert
        assertEquals(address, foundAddress);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding an address by id that does not exist")
    void testFindAddressByIdWhenAddressNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> addressService.getOneById(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test creating an address")
    void testCreateAddress() {
        //arrange
        AddressEntity address = new AddressEntity();
        address.setId(1L);
        address.setUserId("testUser");
        when(repository.save(address)).thenReturn(address);
        //act
        AddressEntity createdAddress = addressService.create(address);
        //assert
        assertEquals(address, createdAddress);
        verify(repository).save(address);
    }

    @Test
    @DisplayName("Test updating an address")
    void testUpdateAddress() {
        //arrange
        AddressEntity address = new AddressEntity();
        address.setId(1L);
        address.setUserId("testUser");
        when(repository.save(address)).thenReturn(address);
        //act
        AddressEntity updatedAddress = addressService.update(1L, address);
        //assert
        assertEquals(address, updatedAddress);
        verify(repository).save(address);
    }

    @Test
    @DisplayName("Test deleting an address")
    void testDeleteAddress() {
        //arrange
        AddressEntity address = new AddressEntity();
        address.setId(1L);
        address.setUserId("testUser");
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(address));
        //act
        addressService.delete(1L);
        //assert
        verify(repository).delete(address);
    }

    @Test
    @DisplayName("Test deleting an address that does not exist")
    void testDeleteAddressThatDoesNotExist() {
        //arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> addressService.delete(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding all addresses")
    void testFindAllAddresses() {
        //arrange
        AddressEntity address = new AddressEntity();
        address.setId(1L);
        address.setUserId("testUser");
        when(repository.findAll()).thenReturn(List.of(address));
        //act
        List<AddressEntity> addresses = addressService.getAll();
        //assert
        assertEquals(address, addresses.get(0));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test finding all addresses when there are none")
    void testFindAllAddressesWhenThereAreNone() {
        //arrange
        when(repository.findAll()).thenReturn(List.of());
        //act
        List<AddressEntity> addresses = addressService.getAll();
        //assert
        assertEquals(0, addresses.size());
        verify(repository).findAll();
    }
}