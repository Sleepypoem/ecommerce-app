package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.CheckoutItemRepository;
import com.sleepypoem.commerceapp.utils.factories.impl.CheckoutItemFactory;
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
class CheckoutItemServiceTest {

    CheckoutItemService service;

    @Mock
    CheckoutItemRepository repository;

    CheckoutItemFactory factory;

    @BeforeEach
    void setUp() {
        service = new CheckoutItemService(repository);
        factory = new CheckoutItemFactory();
    }

    @Test
    @DisplayName("Test creating a checkout item")
    void testCreateCheckoutItemWhenOk() {
        //arrange
        CheckoutItemEntity itemEntity = factory.create();
        when(repository.save(any(CheckoutItemEntity.class))).thenReturn(itemEntity);
        //act
        CheckoutItemEntity checkoutItem = service.create(itemEntity);
        //assert
        assertThat(checkoutItem, equalTo(itemEntity));
        verify(repository).save(itemEntity);
    }

    @Test
    @DisplayName("Test creating a list of checkout items")
    void testCreateListOfCheckoutItemsWhenOk() {
        //arrange
        CheckoutItemEntity entity = factory.create();
        List<CheckoutItemEntity> entityList = List.of(entity);
        when(repository.save(entity)).thenReturn(entity);
        //act
        List<CheckoutItemEntity> createdCheckouItems = service.create(entityList);
        //assert
        assertThat(createdCheckouItems, is(entityList));
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test getting checkout items by checkout id")
    void testGetCheckoutItemsByCheckoutIdWhenOk() {
        //arrange
        List<CheckoutItemEntity> itemEntities = factory.createList(Math.toIntExact(DEFAULT_TOTAL_ELEMENTS));
        when(repository.findByCheckoutId(eq(1L), any())).thenReturn(new PageImpl<>(itemEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS));
        //act
        Page<CheckoutItemEntity> result = service.getByCheckoutIdPaginatedAndSorted(1L, DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(itemEntities, result.getContent()),
                () -> assertEquals(DEFAULT_FIRST_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getPageable().getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getPageable().getSort().getOrderFor("id").getDirection().name()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, result.getTotalElements())
        );
        verify(repository).findByCheckoutId(1L, DEFAULT_PAGEABLE_AT_FIRST_PAGE);
    }

    @Test
    @DisplayName("Test getting checkout items by checkout id when checkout items not found")
    void testGetCheckoutItemsByCheckoutIdWhenNotFound() {
        //arrange
        when(repository.findByCheckoutId(eq(1L), any())).thenReturn(new PageImpl<>(List.of(), DEFAULT_PAGEABLE_AT_FIRST_PAGE, ZERO_TOTAL_ELEMENTS));
        //act
        Page<CheckoutItemEntity> result = service.getByCheckoutIdPaginatedAndSorted(1L, DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertTrue(result.isEmpty()),
                () -> assertEquals(DEFAULT_FIRST_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getPageable().getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getPageable().getSort().getOrderFor("id").getDirection().name()),
                () -> assertEquals(ZERO_TOTAL_ELEMENTS, result.getTotalElements())
        );
        verify(repository).findByCheckoutId(1L, DEFAULT_PAGEABLE_AT_FIRST_PAGE);
    }

    @Test
    @DisplayName("Test updating a checkout item")
    void testUpdateCheckoutItemWhenOk() {
        //arrange
        CheckoutItemEntity checkoutItem = factory.create();
        long id = checkoutItem.getId();
        when(repository.save(checkoutItem)).thenReturn(checkoutItem);
        //act
        CheckoutItemEntity result = service.update(id, checkoutItem);
        //assert
        assertThat(result, is(checkoutItem));
        verify(repository).save(checkoutItem);
    }

    @Test
    @DisplayName("Test finding a checkout item by id")
    void testFindCheckoutItemByIdWhenOk() {
        //arrange
        CheckoutItemEntity entity = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        //act
        CheckoutItemEntity foundCheckoutItem = service.getOneById(1L);
        //assert
        assertThat(foundCheckoutItem, is(equalTo(entity)));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test deleting a checkout item by id")
    void testDeleteCheckoutItemWhenOk() {
        //arrange
        CheckoutItemEntity entity = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        //act
        boolean result = service.deleteById(1L);
        //assert
        assertThat(result, is(true));
        verify(repository).findById(1L);
        verify(repository).delete(entity);
    }

    @Test
    @DisplayName("Test deleting a checkout item by id when checkout item not found")
    void testDeleteCheckoutItemWhenNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.deleteById(1L));
        assertThat(ex.getMessage(), is("CheckoutItem with id 1 not found"));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test deleting an checkout item when delete throws an exception")
    void testDeleteCheckoutItemWhenDeleteThrowsException() {
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
    @DisplayName("Test finding all checkout items")
    void testFindAllCheckoutItemsWhenOk() {
        //arrange
        List<CheckoutItemEntity> entities = factory.createList(50);
        when(repository.findAll()).thenReturn(entities);
        //act
        List<CheckoutItemEntity> foundCheckoutItems = service.getAll();
        //assert
        assertThat(foundCheckoutItems, equalTo(entities));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test getting a list of Checkout items paginated and sorted")
    void testGetAddressesPaginatedAndSortedWhenOk() {
        //arrange
        var addresses = factory.createList(50);
        when(repository.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(addresses, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS)
        );
        //act
        var result = service.getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(addresses, result.getContent()),
                () -> assertEquals(DEFAULT_FIRST_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, result.getTotalElements()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getSort().getOrderFor("id").getDirection().name())
        );
        verify(repository).findAll(DEFAULT_PAGEABLE_AT_FIRST_PAGE);
    }

    @Test
    @DisplayName("Test finding all checkout items when no checkout items found")
    void testEmptyListWhenNoCheckoutItemFound() {
        //arrange
        when(repository.findAll()).thenReturn(List.of());
        //act
        List<CheckoutItemEntity> foundCheckoutItems = service.getAll();
        //assert
        assertThat(foundCheckoutItems, is(empty()));
        assertEquals(java.util.List.of(), foundCheckoutItems);
    }

    @Test
    @DisplayName("Test modify the quantity of an item in the checkout")
    void testModifyQuantity() {
        //arrange
        CheckoutItemEntity entity = factory.create();
        long id = entity.getId();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(repository.save(any(CheckoutItemEntity.class))).thenReturn(entity);
        //act
        CheckoutItemEntity modifiedItem = service.modifyQuantity(id, 5);
        //assert
        assertThat(modifiedItem.getQuantity(), is(5));
        verify(repository).findById(id);
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test modify the quantity of an item in the checkout when item not found")
    void testModifyQuantityWhenItemNotFound() {
        //arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.modifyQuantity(1L, 5));
        verify(repository).findById(1L);
    }
}