package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.*;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.CheckoutRepository;
import com.sleepypoem.commerceapp.utils.factories.impl.AddressFactory;
import com.sleepypoem.commerceapp.utils.factories.impl.CheckoutFactory;
import com.sleepypoem.commerceapp.utils.factories.impl.CheckoutItemFactory;
import com.sleepypoem.commerceapp.utils.factories.impl.PaymentMethodFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
class CheckoutServiceTest {

    CheckoutService service;

    @Mock
    CheckoutRepository repository;

    @Mock
    CheckoutItemService checkoutItemService;

    @Mock
    ProductService productService;

    CheckoutFactory factory;

    @BeforeEach
    void setUp() {
        service = new CheckoutService(repository, checkoutItemService, productService);
        factory = new CheckoutFactory();

    }


    @Test
    @DisplayName("Test creating a checkout item")
    void testCreateCheckoutWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemEntity item = entity.getItems().get(0);
        ProductEntity productEntity = item.getProduct();
        when(repository.save(any(CheckoutEntity.class))).thenReturn(entity);
        when(productService.decreaseStock(anyLong(), anyInt())).thenReturn(productEntity);
        //act
        CheckoutEntity createdCheckout = service.create(entity);
        //assert
        assertThat(createdCheckout, equalTo(entity));
        verify(repository).save(entity);
        verify(productService).decreaseStock(productEntity.getId(), item.getQuantity());
    }

    @Test
    @DisplayName("Test created checkout have status PENDING")
    void testCreatedCheckoutHaveStatusPending() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemEntity item = entity.getItems().get(0);
        ProductEntity productEntity = item.getProduct();
        when(repository.save(any(CheckoutEntity.class))).thenReturn(entity);
        when(productService.decreaseStock(anyLong(), anyInt())).thenReturn(productEntity);
        //act
        CheckoutEntity createdCheckout = service.create(entity);
        //assert
        assertThat(createdCheckout, equalTo(entity));
        assertThat(createdCheckout.getStatus(), is(CheckoutStatus.PENDING));
    }

    @Test
    @DisplayName("Test canceled checkout have status CANCELED")
    void testCanceledCheckoutHaveStatusCanceled() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemEntity item = entity.getItems().get(0);
        ProductEntity productEntity = item.getProduct();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(repository.save(any(CheckoutEntity.class))).thenReturn(entity);
        when(productService.increaseStock(anyLong(), anyInt())).thenReturn(productEntity);
        //act
        service.setStatusToCanceled(entity.getId());
        //assert
        ArgumentCaptor<CheckoutEntity> captor = ArgumentCaptor.forClass(CheckoutEntity.class);
        verify(repository).save(captor.capture());
        verify(productService).increaseStock(productEntity.getId(), item.getQuantity());
        verify(repository).findById(entity.getId());
        assertThat(captor.getValue().getStatus(), is(CheckoutStatus.CANCELLED));
    }

    @Test
    @DisplayName("Test completed checkout have status COMPLETED")
    void testCompletedCheckoutHaveStatusCompleted() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemEntity item = entity.getItems().get(0);
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(repository.save(any(CheckoutEntity.class))).thenReturn(entity);
        //act
        service.setStatusToCompleted(entity.getId());
        //assert
        ArgumentCaptor<CheckoutEntity> captor = ArgumentCaptor.forClass(CheckoutEntity.class);
        verify(repository).save(captor.capture());
        verify(repository).findById(entity.getId());
        assertThat(captor.getValue().getStatus(), is(CheckoutStatus.COMPLETED));
    }

    @Test
    @DisplayName("Test updating a checkout item")
    void testUpdateCheckoutWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
        long id = entity.getId();
        when(repository.save(any(CheckoutEntity.class))).thenReturn(entity);
        //act
        CheckoutEntity updatedCheckout = service.update(id, entity);
        //assert
        assertThat(updatedCheckout, equalTo(entity));
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test finding a checkout item by id")
    void testFindCheckoutByIdWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        //act
        CheckoutEntity foundCheckout = service.getOneById(1L);
        //assert
        assertThat(foundCheckout, equalTo(entity));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding a checkout item by id when checkout item not found")
    void testFindCheckoutByIdWhenCheckoutNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.getOneById(1L));
    }

    @Test
    @DisplayName("Test deleting a checkout item by id")
    void testDeleteCheckoutWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
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
    void testDeleteCheckoutWhenCheckoutNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.deleteById(1L));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding all checkout items")
    void testFindAllCheckoutsWhenOk() {
        //arrange
        List<CheckoutEntity> checkoutEntities = factory.createList(50);
        when(repository.findAll()).thenReturn(checkoutEntities);
        //act
        List<CheckoutEntity> foundCheckouts = service.getAll();
        //assert
        assertThat(foundCheckouts, equalTo(checkoutEntities));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test getting a list of Checkouts paginated and sorted")
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
    @DisplayName("Test finding all checkout items when no checkout items found")
    void testEmptyListWhenNoCheckoutsFound() {
        //arrange
        when(repository.findAll()).thenReturn(java.util.List.of());
        //act
        List<CheckoutEntity> foundCheckouts = service.getAll();
        //assert
        assertThat(foundCheckouts, is(empty()));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test deleting an item from cart")
    void testDeleteItemFromCartWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemEntity itemEntity = entity.getItems().get(0);
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(checkoutItemService.getOneById(anyLong())).thenReturn(itemEntity);
        when(checkoutItemService.deleteById(anyLong())).thenReturn(true);
        //act
        service.removeItem(1L, 1L);
        //assert
        verify(repository).findById(1L);
        verify(checkoutItemService).getOneById(1L);
        verify(checkoutItemService).deleteById(1L);
    }

    @Test
    @DisplayName("Test deleting a checkout when delete throws an exception")
    void testDeleteCheckoutWhenDeleteThrowsException() {
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
    @DisplayName("Test deleting an item from cart when checkout item not found")
    void testDeleteItemFromCartWhenCheckoutItemNotFound() {
        //arrange
        CheckoutEntity entity = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(checkoutItemService.getOneById(anyLong())).thenThrow(MyEntityNotFoundException.class);
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.removeItem(1L, 1L));
        verify(repository).findById(1L);
        verify(checkoutItemService).getOneById(1L);
    }

    @Test
    @DisplayName("Test deleting an item from cart when checkout not found")
    void testDeleteItemFromCartWhenCheckoutNotFound() {
        //arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.removeItem(1L, 1L));
        assertThat(ex.getMessage(), is("Checkout with id 1 not found"));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test modifying checkout item quantity when stock needs to be increased")
    void testModifyCheckoutItemQuantityWhenStockNeedsToIncrease() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemEntity itemEntity = entity.getItems().get(0);
        ProductEntity productEntity = itemEntity.getProduct();
        productEntity.setStock(10);
        itemEntity.setQuantity(10);
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(productService.increaseStock(anyLong(), anyInt())).thenReturn(productEntity);
        when(checkoutItemService.getOneById(anyLong())).thenReturn(itemEntity);
        //act
        service.modifyItemQuantity(1L, 1L, 1);
        //assert
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(productService).increaseStock(eq(productEntity.getId()), captor.capture());
        verify(repository).findById(1L);
        verify(checkoutItemService).getOneById(1L);
        verify(checkoutItemService).modifyQuantity(1L, 1);
        assertThat(captor.getValue(), is(9));
    }

    @Test
    @DisplayName("Test modifying checkout item quantity when stock needs to be decreased")
    void testModifyCheckoutItemQuantityWhenStockNeedsToDecrease() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemEntity itemEntity = entity.getItems().get(0);
        ProductEntity productEntity = itemEntity.getProduct();
        productEntity.setStock(10);
        itemEntity.setQuantity(1);
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(productService.decreaseStock(anyLong(), anyInt())).thenReturn(productEntity);
        when(checkoutItemService.getOneById(anyLong())).thenReturn(itemEntity);
        //act
        service.modifyItemQuantity(1L, 1L, 6);
        //assert
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(productService).decreaseStock(eq(productEntity.getId()), captor.capture());
        verify(repository).findById(1L);
        verify(checkoutItemService).getOneById(1L);
        verify(checkoutItemService).modifyQuantity(1L, 6);
        assertThat(captor.getValue(), is(5));
    }

    @Test
    @DisplayName("Test deleting an item from cart when item does not belong to checkout")
    void testDeleteItemFromCartWhenItemDoesNotBelongToCheckout() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemFactory checkoutItemFactory = new CheckoutItemFactory();
        CheckoutItemEntity itemEntity = checkoutItemFactory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(checkoutItemService.getOneById(anyLong())).thenReturn(itemEntity);
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> service.removeItem(1L, 1L));
        assertThat(ex.getMessage(), is("Item not found in this checkout"));
        verify(repository).findById(1L);
        verify(checkoutItemService).getOneById(1L);
    }

    @Test
    @DisplayName("Test finding all checkouts by user id")
    void testFindAllCheckoutsByUserIdWhenOk() {
        //arrange
        List<CheckoutEntity> checkoutEntities = factory.createList(50);
        when(repository.findAllByUserId(eq("test"), any())).thenReturn(new PageImpl<>(checkoutEntities, DEFAULT_PAGEABLE, DEFAULT_TOTAL_ELEMENTS));
        //act
        Page<CheckoutEntity> foundCheckouts = service.getAllPaginatedAndSortedByUserId("test", DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(checkoutEntities, foundCheckouts.getContent()),
                () -> assertEquals(DEFAULT_PAGE, foundCheckouts.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, foundCheckouts.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, foundCheckouts.getTotalElements()),
                () -> assertEquals(DEFAULT_SORT_BY, foundCheckouts.getPageable().getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, foundCheckouts.getPageable().getSort().getOrderFor("id").getDirection().name())
        );
        verify(repository).findAllByUserId("test", DEFAULT_PAGEABLE);
    }

    @Test
    @DisplayName(("Test getting a checkout items by checkout id"))
    void testGetCheckoutItemsByCheckoutIdWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
        when(checkoutItemService.getByCheckoutIdPaginatedAndSorted(anyLong(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(
                new PageImpl<>(entity.getItems(), DEFAULT_PAGEABLE, DEFAULT_TOTAL_ELEMENTS)
        );
        //act
        Page<CheckoutItemEntity> foundItems = service.getAllItemsPaginatedAndSorted(entity.getId(), DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(entity.getItems(), foundItems.getContent()),
                () -> assertEquals(DEFAULT_PAGE, foundItems.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, foundItems.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, foundItems.getTotalElements()),
                () -> assertEquals(DEFAULT_SORT_BY, foundItems.getPageable().getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, foundItems.getPageable().getSort().getOrderFor("id").getDirection().name())
        );
        verify(checkoutItemService).getByCheckoutIdPaginatedAndSorted(entity.getId(), DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
    }

    @Test
    @DisplayName("Test finding all checkouts by user id when no checkouts found")
    void testEmptyListWhenNoCheckoutsFoundByUserId() {
        //arrange
        when(repository.findAllByUserId(anyString(), any(Pageable.class))).thenReturn(
                new PageImpl<>(List.of(), DEFAULT_PAGEABLE, ZERO_TOTAL_ELEMENTS)
        );
        //act
        Page<CheckoutEntity> foundCheckouts = service.getAllPaginatedAndSortedByUserId("test", DEFAULT_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(List.of(), foundCheckouts.getContent()),
                () -> assertEquals(DEFAULT_PAGE, foundCheckouts.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, foundCheckouts.getPageable().getPageSize()),
                () -> assertEquals(ZERO_TOTAL_ELEMENTS, foundCheckouts.getTotalElements()),
                () -> assertEquals(DEFAULT_SORT_BY, foundCheckouts.getPageable().getSort().getOrderFor(DEFAULT_SORT_BY).getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, foundCheckouts.getPageable().getSort().getOrderFor(DEFAULT_SORT_BY).getDirection().name())
        );
    }

    @Test
    @DisplayName("Test adding a default payment method to a checkout")
    void testAddDefaultPaymentMethodToCheckoutWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodFactory().create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(repository.save(any(CheckoutEntity.class))).thenReturn(entity);
        //act
        CheckoutEntity updatedCheckout = service.addPreferredPaymentMethod(entity.getId(), paymentMethodEntity);
        //assert
        assertThat(updatedCheckout.getPaymentMethod(), is(paymentMethodEntity));
        verify(repository).findById(entity.getId());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test adding a default address to a checkout")
    void testAddDefaultAddressToCheckoutWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
        AddressFactory addressFactory = new AddressFactory();
        AddressEntity addressEntity = addressFactory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(repository.save(any(CheckoutEntity.class))).thenReturn(entity);
        //act
        CheckoutEntity updatedCheckout = service.addPreferredAddress(entity.getId(), addressEntity);
        //assert
        assertThat(updatedCheckout.getAddress(), is(addressEntity));
        verify(repository).findById(entity.getId());
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test adding a checkout item to a checkout")
    void testAddCheckoutItemToCheckoutWhenOk() {
        //arrange
        CheckoutEntity entity = factory.create();
        CheckoutItemEntity checkoutItemEntity = new CheckoutItemFactory().create();
        ProductEntity productEntity = checkoutItemEntity.getProduct();
        entity.setItems(List.of(checkoutItemEntity));
        long checkoutId = entity.getId();
        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(productService.decreaseStock(anyLong(), anyInt())).thenReturn(productEntity);
        when(checkoutItemService.create(any(CheckoutItemEntity.class))).thenReturn(checkoutItemEntity);
        //act
        CheckoutEntity updatedCheckout = service.addItems(checkoutId, List.of(checkoutItemEntity));
        //assert
        assertThat(updatedCheckout.getItems(), hasItem(checkoutItemEntity));
        verify(repository, times(2)).findById(checkoutId);
        verify(productService).decreaseStock(productEntity.getId(), checkoutItemEntity.getQuantity());
        verify(checkoutItemService).create(checkoutItemEntity);
    }
}