package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.CheckoutItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CheckoutItemServiceTest {

    CheckoutItemService service;

    @Mock
    CheckoutItemRepository repository;

    CheckoutItemEntity entity;

    @BeforeEach
    void setUp() {
        service = new CheckoutItemService(repository);
        entity = new CheckoutItemEntity();
    }

    @Test
    @DisplayName("Test creating a checkout item")
    void testCreateProduct() {
        //arrange
        entity.setId(1L);
        entity.setQuantity(10);
        when(repository.save(entity)).thenReturn(entity);
        //act
        CheckoutItemEntity createdProduct = service.create(entity);
        //assert
        assertEquals(entity, createdProduct);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test creating a list of checkout items")
    void testCreateListOfProducts() {
        //arrange
        entity.setId(1L);
        entity.setQuantity(10);
        when(repository.save(entity)).thenReturn(entity);
        //act
        List<CheckoutItemEntity> createdProducts = service.create(List.of(entity));
        //assert
        assertEquals(List.of(entity), createdProducts);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test getting checkout items by checkout id")
    void testGetProductsByCheckoutId() {
        //arrange
        entity.setId(1L);
        entity.setQuantity(10);
        when(repository.findByCheckoutId(eq(1L), any())).thenReturn(new PageImpl<>(List.of(entity)));
        //act
        Page<CheckoutItemEntity> foundProducts = service.getByCheckoutIdPaginatedAndSorted(1L, 1, 10, "id", "asc");
        //assert
        assertEquals(List.of(entity), foundProducts.getContent());
        verify(repository).findByCheckoutId(1L, any());
    }

    @Test
    @DisplayName("Test updating a checkout item")
    void testUpdateProduct() {
        //arrange
        entity.setId(1L);
        entity.setQuantity(10);
        when(repository.save(entity)).thenReturn(entity);
        //act
        CheckoutItemEntity updatedProduct = service.update(1L, entity);
        //assert
        assertEquals(entity, updatedProduct);
    }

    @Test
    @DisplayName("Test finding a checkout item by id")
    void testFindProductById() {
        //arrange
        entity.setId(1L);
        entity.setQuantity(10);
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(entity));
        //act
        CheckoutItemEntity foundProduct = service.getOneById(1L);
        //assert
        assertEquals(entity, foundProduct);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding a checkout item by id when checkout item not found")
    void testFindProductByIdWhenProductNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.getOneById(1L));
    }

    @Test
    @DisplayName("Test deleting a checkout item by id")
    void testDeleteProduct() {
        //arrange
        entity.setId(1L);
        entity.setQuantity(10);
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(entity));
        //act
        service.delete(1L);
        //assert
        verify(repository).delete(entity);
    }

    @Test
    @DisplayName("Test deleting a checkout item by id when checkout item not found")
    void testDeleteProductWhenProductNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.delete(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding all checkout items")
    void testFindAllProducts() {
        //arrange
        entity.setId(1L);
        entity.setQuantity(10);
        when(repository.findAll()).thenReturn(java.util.List.of(entity));
        //act
        List<CheckoutItemEntity> foundProducts = service.getAll();
        //assert
        assertEquals(java.util.List.of(entity), foundProducts);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test finding all checkout items when no checkout items found")
    void testEmptyListWhenNoProductsFound() {
        //arrange
        when(repository.findAll()).thenReturn(java.util.List.of());
        //act
        List<CheckoutItemEntity> foundProducts = service.getAll();
        //assert
        assertEquals(java.util.List.of(), foundProducts);
    }

    @Test
    @DisplayName("Test modify the quantity of an item in the checkout")
    void testModifyQuantity() {
        //arrange
        entity.setId(1L);
        entity.setQuantity(10);
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);
        //act
        CheckoutItemEntity modifiedItem = service.modifyQuantity(1L, 5);
        //assert
        assertEquals(5, modifiedItem.getQuantity());
        verify(repository).findById(1L);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test modify the quantity of an item in the checkout when item not found")
    void testModifyQuantityWhenItemNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.modifyQuantity(1L, 5));
        verify(repository).findById(1L);
    }
}