package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.*;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.CheckoutRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    CheckoutService service;

    @Mock
    CheckoutRepository repository;

    @Mock
    CheckoutItemService checkoutItemService;

    @Mock
    ProductService productService;

    CheckoutEntity entity;

    ProductEntity productEntity;

    CheckoutItemEntity checkoutItemEntity;

    AddressEntity addressEntity;

    PaymentMethodEntity paymentMethodEntity;

    @BeforeEach
    void setUp() {
        service = new CheckoutService(repository, checkoutItemService, productService);
        entity = new CheckoutEntity();
        productEntity = new ProductEntity();
        productEntity.setId(1L);
        productEntity.setName("test");
        productEntity.setDescription("test");
        productEntity.setPrice(10.0);
        productEntity.setStock(10);
        checkoutItemEntity = new CheckoutItemEntity();
        checkoutItemEntity.setId(1L);
        checkoutItemEntity.setProduct(productEntity);
        checkoutItemEntity.setQuantity(1);
        addressEntity = new AddressEntity();
        addressEntity.setId(1L);
        addressEntity.setCountry("test");
        paymentMethodEntity = new PaymentMethodEntity();
        paymentMethodEntity.setId(1L);
        paymentMethodEntity.setUserId("test");

    }


    @Test
    @DisplayName("Test creating a checkout item")
    void testCreateCheckout() {
        //arrange
        entity.setId(1L);
        entity.setUserId("test");
        when(repository.save(entity)).thenReturn(entity);
        //act
        CheckoutEntity createdCheckout = service.create(entity);
        //assert
        assertEquals(entity, createdCheckout);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test updating a checkout item")
    void testUpdateCheckout() {
        //arrange
        entity.setId(1L);
        entity.setUserId("test");
        when(repository.save(entity)).thenReturn(entity);
        //act
        CheckoutEntity updatedCheckout = service.update(1L, entity);
        //assert
        assertEquals(entity, updatedCheckout);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test finding a checkout item by id")
    void testFindCheckoutById() {
        //arrange
        entity.setId(1L);
        entity.setUserId("test");
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(entity));
        //act
        CheckoutEntity foundCheckout = service.getOneById(1L);
        //assert
        assertEquals(entity, foundCheckout);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding a checkout item by id when checkout item not found")
    void testFindCheckoutByIdWhenCheckoutNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.getOneById(1L));
    }

    @Test
    @DisplayName("Test deleting a checkout item by id")
    void testDeleteCheckout() {
        //arrange
        entity.setId(1L);
        entity.setUserId("test");
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(entity));
        //act
        service.delete(1L);
        //assert
        verify(repository).deleteById(1L);
    }

    @Test
    @DisplayName("Test deleting a checkout item by id when checkout item not found")
    void testDeleteCheckoutWhenCheckoutNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.delete(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding all checkout items")
    void testFindAllCheckouts() {
        //arrange
        entity.setId(1L);
        entity.setUserId("test");
        when(repository.findAll()).thenReturn(java.util.List.of(entity));
        //act
        List<CheckoutEntity> foundCheckouts = service.getAll();
        //assert
        assertEquals(java.util.List.of(entity), foundCheckouts);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test finding all checkout items when no checkout items found")
    void testEmptyListWhenNoCheckoutsFound() {
        //arrange
        when(repository.findAll()).thenReturn(java.util.List.of());
        //act
        List<CheckoutEntity> foundCheckouts = service.getAll();
        //assert
        assertEquals(java.util.List.of(), foundCheckouts);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test finding all checkouts by user id")
    void testFindAllCheckoutsByUserId() {
        //arrange
        entity.setId(1L);
        entity.setUserId("test");
        when(repository.findAllByUserId(eq("test"), any())).thenReturn(new PageImpl<>(java.util.List.of(entity)));
        //act
        Page<CheckoutEntity> foundCheckouts = service.getAllPaginatedAndSortedByUserId("test", 1, 10, "id", "asc");
        //assert
        assertEquals(java.util.List.of(entity), foundCheckouts.getContent());
        verify(repository).findAllByUserId(eq("test"), any());
    }

    @Test
    @DisplayName("Test finding all checkouts by user id when no checkouts found")
    @Disabled
    void testEmptyListWhenNoCheckoutsFoundByUserId() {

    }

    @Test
    @DisplayName("Test adding a checkout item to a checkout")
    void testAddCheckoutItemToCheckout() {
        //arrange
        entity.setId(1L);
        entity.setUserId("test");
        entity.setItems(new ArrayList<>());

        when(repository.findById(1L)).thenReturn(java.util.Optional.of(entity));
        when(productService.getOneById(1L)).thenReturn(productEntity);
        //act
        CheckoutEntity updatedCheckout = service.addItems(1L, java.util.List.of(checkoutItemEntity));
        //assert
        assertEquals(List.of(checkoutItemEntity), updatedCheckout.getItems());
        verify(repository).findById(1L);
        verify(repository).save(entity);
    }
}