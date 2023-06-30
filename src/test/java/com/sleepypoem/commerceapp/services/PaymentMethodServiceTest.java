package com.sleepypoem.commerceapp.services;


import com.sleepypoem.commerceapp.config.payment.StripeFacade;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyStripeException;
import com.sleepypoem.commerceapp.repositories.PaymentMethodRepository;
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
class PaymentMethodServiceTest {

    PaymentMethodService service;

    @Mock
    PaymentMethodRepository repository;

    @Mock
    StripeFacade stripeFacade;

    PaymentMethodFactory factory;

    @BeforeEach
    void setUp() {
        service = new PaymentMethodService(repository, stripeFacade);
        factory = new PaymentMethodFactory();
    }

    @Test
    @DisplayName("Test creating a Card")
    void testCreateCardWhenOk() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = factory.create();
        paymentMethodEntity.setUserId("userId");

        when(stripeFacade.createPaymentMethod("userId", "cardToken")).thenReturn(paymentMethodEntity);
        when(repository.save(any(PaymentMethodEntity.class))).thenReturn(paymentMethodEntity);
        //act
        PaymentMethodEntity result = service.createCard("cardToken", "userId");
        //assert
        assertEquals(paymentMethodEntity, result);
        ArgumentCaptor<PaymentMethodEntity> argumentCaptor = ArgumentCaptor.forClass(PaymentMethodEntity.class);
        verify(stripeFacade).createPaymentMethod("userId", "cardToken");
        verify(repository).save(argumentCaptor.capture());
        String userId = argumentCaptor.getValue().getUserId();
        assertThat(userId, is(equalTo("userId")));
    }

    @Test
    @DisplayName("Test creating a Card when stripe throws an exception")
    void testCreateCardWhenStripeThrowsException() {
        //arrange
        when(stripeFacade.createPaymentMethod("userId", "cardToken")).thenThrow(new MyStripeException("stripe exception"));
        //act
        //assert
        var ex = assertThrows(MyStripeException.class, () -> service.createCard("cardToken", "userId"));
        assertThat(ex.getMessage(), is("stripe exception"));
        verify(stripeFacade).createPaymentMethod("userId", "cardToken");
        verifyNoInteractions(repository);
    }

    @Test
    @DisplayName("Test updating a Card")
    void testUpdateCardWhenOk() {
        //arrange
        PaymentMethodEntity entity = factory.create();
        long id = entity.getId();

        when(repository.findById(anyLong())).thenReturn(Optional.of(entity));
        when(stripeFacade.updatePaymentMethod(any(PaymentMethodEntity.class), eq("cardToken"))).thenReturn(entity);
        when(repository.save(any(PaymentMethodEntity.class))).thenReturn(entity);
        //act
        PaymentMethodEntity result = service.updateCard(id, "cardToken");
        //assert
        assertThat(result, is(equalTo(entity)));
        verify(repository).findById(id);
        verify(stripeFacade).updatePaymentMethod(entity, "cardToken");
        verify(repository).save(entity);
    }

    @Test
    @DisplayName("Test updating a card when payment method not found")
    void testUpdateCardWhenPaymentMethodNotFound() {
        //arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.updateCard(1L, "cardToken"));
        assertThat(ex.getMessage(), is("PaymentMethod with id 1 not found"));
    }

    @Test
    @DisplayName("Test updating a card when stripe throws an exception")
    void testUpdateCardWhenStripeThrowsException() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = factory.create();
        String message = "Error while creating payment method. Error message: " + "StripeException";

        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentMethodEntity));
        when(stripeFacade.updatePaymentMethod(any(PaymentMethodEntity.class), eq("cardToken"))).thenThrow(new MyStripeException(message));
        //act
        //assert
        var ex = assertThrows(MyStripeException.class, () -> service.updateCard(1L, "cardToken"));
        assertThat(ex.getMessage(), is(message));
    }

    @Test
    @DisplayName("Test deleting a card")
    void testDeleteCardWhenOk() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = factory.create();

        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentMethodEntity));
        //act
        boolean result = service.deleteCard(paymentMethodEntity.getId());
        //assert
        assertThat(result, is(true));
        verify(repository).findById(paymentMethodEntity.getId());
        verify(stripeFacade).deletePaymentMethod(paymentMethodEntity.getPaymentId());
        verify(repository).delete(paymentMethodEntity);
    }

    @Test
    @DisplayName("Test deleting a card when payment method not found")
    void testDeleteCardWhenPaymentMethodNotFound() {
        //arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.deleteCard(1L));
        assertThat(ex.getMessage(), is("PaymentMethod with id 1 not found"));
    }

    @Test
    @DisplayName("Test deleting a card when stripe throws an exception")
    void testDeleteCardWhenStripeThrowsException() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = factory.create();
        String message = "Error while deleting payment method. Error message: " + "StripeException";

        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentMethodEntity));
        doThrow(new MyStripeException(message)).when(stripeFacade).deletePaymentMethod(anyString());
        //act
        boolean result = service.deleteCard(paymentMethodEntity.getId());
        //assert
        assertThat(result, is(false));
        verify(repository).findById(paymentMethodEntity.getId());
        verify(stripeFacade).deletePaymentMethod(paymentMethodEntity.getPaymentId());
    }

    @Test
    @DisplayName("Test deleting a payment method when delete throws an exception")
    void testDeletePaymentMethodWhenDeleteThrowsException() {
        //arrange
        var paymentMethod = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentMethod));
        doThrow(new RuntimeException("")).when(repository).delete(paymentMethod);
        //act
        boolean result = service.deleteById(1L);
        //assert
        assertThat(result, is(false));
        verify(repository).findById(1L);
        verify(repository).delete(paymentMethod);
    }

    @Test
    @DisplayName("Test getting a card")
    void testGetCardWhenOk() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = factory.create();

        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentMethodEntity));
        //act
        PaymentMethodEntity result = service.getOneById(paymentMethodEntity.getId());
        //assert
        assertThat(result, is(equalTo(paymentMethodEntity)));
        verify(repository).findById(paymentMethodEntity.getId());
    }

    @Test
    @DisplayName("Test getting a card when payment method not found")
    void testGetCardWhenPaymentMethodNotFound() {
        //arrange
        when(repository.findById(anyLong())).thenReturn(Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.getOneById(1L));
        assertThat(ex.getMessage(), is("PaymentMethod with id 1 not found"));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test getting a list of cards by user id")
    void testGetCardsByUserIdWhenOk() {
        //arrange
        List<PaymentMethodEntity> paymentMethodEntities = factory.createList(Math.toIntExact(DEFAULT_TOTAL_ELEMENTS));
        when(repository.findByUserId(anyString(), any(Pageable.class))).thenReturn(
                new PageImpl<>(paymentMethodEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS)
        );
        //act
        Page<PaymentMethodEntity> result = service.getAllPaginatedAndSortedByUserId("userId", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, result.getTotalElements()),
                () -> assertEquals(5, result.getTotalPages()),
                () -> assertEquals(DEFAULT_SIZE, result.getSize()),
                () -> assertEquals(DEFAULT_FIRST_PAGE, result.getNumber()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getPageable().getSort().getOrderFor(DEFAULT_SORT_BY).getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getPageable().getSort().getOrderFor(DEFAULT_SORT_BY).getDirection().name()),
                () -> assertEquals(paymentMethodEntities, result.getContent()));
        verify(repository).findByUserId("userId", DEFAULT_PAGEABLE_AT_FIRST_PAGE);
    }

    @Test
    @DisplayName("Test getting a list of cards by user id when user not found or has no cards")
    void testGetCardsByUserIdWhenUserNotFoundOrHasNoCards() {
        //arrange
        when(repository.findByUserId(anyString(), any(Pageable.class))).thenReturn(Page.empty());
        //act
        Page<PaymentMethodEntity> result = service.getAllPaginatedAndSortedByUserId("userId", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertThat(result.getContent(), is(empty()));
    }

    @Test
    @DisplayName("Test getting all cards")
    void testGetAllCardsWhenOk() {
        //arrange
        List<PaymentMethodEntity> paymentMethodEntities = factory.createList(50);

        when(repository.findAll()).thenReturn(paymentMethodEntities);
        //act
        List<PaymentMethodEntity> result = service.getAll();
        //assert
        assertThat(result, is(equalTo(paymentMethodEntities)));
    }

    @Test
    @DisplayName("Test getting a list of payment methods paginated and sorted")
    void testGetAllCardsPaginatedAndSortedWhenOk() {
        //arrange
        var paymentMethods = factory.createList(50);
        when(repository.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(paymentMethods, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS)
        );
        //act
        var result = service.getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(paymentMethods, result.getContent()),
                () -> assertEquals(DEFAULT_FIRST_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, result.getTotalElements()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getSort().getOrderFor("id").getDirection().name())
        );
        verify(repository).findAll(DEFAULT_PAGEABLE_AT_FIRST_PAGE);
    }

    @Test
    @DisplayName("Test getting all cards when no cards found")
    void testGetAllCardsWhenNoCardsFound() {
        //arrange
        when(repository.findAll()).thenReturn(List.of());
        //act
        List<PaymentMethodEntity> result = service.getAll();
        //assert
        assertThat(result, is(empty()));
    }

}
