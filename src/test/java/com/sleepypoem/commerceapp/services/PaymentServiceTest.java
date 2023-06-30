package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.config.payment.StripeFacade;
import com.sleepypoem.commerceapp.domain.dto.PaymentIntentDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.domain.enums.Currency;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyStripeException;
import com.sleepypoem.commerceapp.repositories.PaymentRepository;
import com.sleepypoem.commerceapp.utils.factories.impl.PaymentFactory;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.sleepypoem.commerceapp.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    PaymentService service;

    @Mock
    PaymentRepository repository;

    @Mock
    StripeFacade stripeFacade;

    @Mock
    CheckoutService checkoutService;

    PaymentFactory factory;

    @BeforeEach
    void setUp() {
        service = new PaymentService(repository, stripeFacade, checkoutService);
        factory = new PaymentFactory();
    }

    @Test
    @DisplayName("Test start payment processing")
    void testStartPaymentWhenOk() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(paymentEntity.getCheckout(), paymentEntity.getUserId(), paymentEntity.getCurrency());
        when(checkoutService.getOneById(anyLong())).thenReturn(paymentEntity.getCheckout());
        when(repository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        //act
        PaymentEntity result = service.startPayment(paymentRequestDto);
        //assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(result.getCheckout(), paymentEntity.getCheckout()),
                () -> assertEquals(result.getUserId(), paymentEntity.getUserId()),
                () -> assertEquals(result.getCurrency(), paymentEntity.getCurrency()),
                () -> assertEquals(result.getStatus(), paymentEntity.getStatus())
        );
        verify(repository).save(any(PaymentEntity.class));
    }

    @Test
    @DisplayName("Test start payment processing when checkout is not found")
    void testStartPaymentWhenCheckoutNotFound() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(paymentEntity.getCheckout(), paymentEntity.getUserId(), paymentEntity.getCurrency());
        doThrow(MyEntityNotFoundException.class).when(checkoutService).getOneById(anyLong());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> service.startPayment(paymentRequestDto));
    }

    @Test
    @DisplayName("Test start payment processing when checkout is not in status PENDING")
    void testStartPaymentWhenCheckoutIsNotPending() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        paymentEntity.getCheckout().setStatus(CheckoutStatus.COMPLETED);
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(paymentEntity.getCheckout(), paymentEntity.getUserId(), paymentEntity.getCurrency());
        when(checkoutService.getOneById(anyLong())).thenReturn(paymentEntity.getCheckout());
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> service.startPayment(paymentRequestDto));
        assertThat(ex.getMessage(), is("Checkout is not pending"));
    }

    @Test
    @DisplayName("Test start payment processing when payment method is not provided")
    void testStartPaymentWhenPaymentMethodIsNotProvided() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        paymentEntity.getCheckout().setPaymentMethod(null);
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(paymentEntity.getCheckout(), paymentEntity.getUserId(), paymentEntity.getCurrency());
        when(checkoutService.getOneById(anyLong())).thenReturn(paymentEntity.getCheckout());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.startPayment(paymentRequestDto));
        assertThat(ex.getMessage(), is("Payment method not set"));
        verify(repository, never()).save(any(PaymentEntity.class));
        verify(checkoutService).getOneById(paymentEntity.getCheckout().getId());
    }

    @Test
    @DisplayName("Test start payment processing when address is not provided")
    void testStartPaymentWhenAddressIsNotProvided() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        paymentEntity.getCheckout().setAddress(null);
        PaymentRequestDto paymentRequestDto = new PaymentRequestDto(paymentEntity.getCheckout(), paymentEntity.getUserId(), paymentEntity.getCurrency());
        when(checkoutService.getOneById(anyLong())).thenReturn(paymentEntity.getCheckout());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.startPayment(paymentRequestDto));
        assertThat(ex.getMessage(), is("Address not set"));
        verify(repository, never()).save(any(PaymentEntity.class));
        verify(checkoutService).getOneById(paymentEntity.getCheckout().getId());
    }

    @Test
    @DisplayName("Test confirm payment")
    void testConfirmPaymentWhenOk() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        PaymentIntentDto paymentIntentDto = new PaymentIntentDto(
                PaymentStatus.SUCCESS.name(),
                "PaymentIntentId",
                paymentEntity.getCheckout().getTotal().longValue()
        );
        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentEntity));
        when(stripeFacade.createAndConfirmPaymentIntent(anyString(), anyString(), anyInt(), anyString())).thenReturn(paymentIntentDto);
        when(repository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        //act
        PaymentEntity result = service.confirmPayment(paymentEntity.getId());
        //assert
        ArgumentCaptor<PaymentEntity> paymentEntityArgumentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);
        verify(repository).save(paymentEntityArgumentCaptor.capture());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(PaymentStatus.SUCCESS, paymentEntityArgumentCaptor.getValue().getStatus())
        );
        verify(repository).findById(paymentEntity.getId());
        verify(stripeFacade).createAndConfirmPaymentIntent(paymentEntity.getCheckout().getPaymentMethod().getStripeUserId(),
                paymentEntity.getCheckout().getPaymentMethod().getPaymentId(),
                paymentEntity.getCheckout().getTotal().multiply(BigDecimal.valueOf(100)).intValue(),
                Currency.USD.name());

    }

    @Test
    @DisplayName("Test confirm payment when payment is not found")
    void testConfirmPaymentWhenPaymentNotFound() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        paymentEntity.getCheckout().setPaymentMethod(null);
        long paymentId = paymentEntity.getId();
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.confirmPayment(paymentId));
        assertEquals("Payment with id " + paymentId + " not found", ex.getMessage());
    }

    @Test
    @DisplayName("Test confirm payment when payment is not in status PROCESSING")
    void testConfirmPaymentWhenPaymentIsNotProcessing() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        paymentEntity.setStatus(PaymentStatus.SUCCESS);
        long paymentId = paymentEntity.getId();
        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentEntity));
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> service.confirmPayment(paymentId));
        assertEquals("Payment is not processing", ex.getMessage());
    }

    @Test
    @DisplayName("Test confirm payment when Stripe throws an exception")
    void testConfirmPaymentWhenStripeThrowsException() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        long paymentId = paymentEntity.getId();
        PaymentIntentDto paymentIntentDto = new PaymentIntentDto(
                PaymentStatus.SUCCESS.name(),
                "PaymentIntentId",
                paymentEntity.getCheckout().getTotal().longValue()
        );
        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentEntity));
        when(stripeFacade.createAndConfirmPaymentIntent(anyString(), anyString(), anyInt(), anyString())).thenThrow(new MyStripeException("Stripe exception"));
        when(repository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        //act
        PaymentEntity result = service.confirmPayment(paymentId);
        //assert
        ArgumentCaptor<PaymentEntity> paymentEntityArgumentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);
        verify(repository).save(paymentEntityArgumentCaptor.capture());
        assertAll(
                () -> assertNotNull(result),
                () -> assertNotNull(paymentEntityArgumentCaptor.getValue()),
                () -> assertEquals(PaymentStatus.FAILED, paymentEntityArgumentCaptor.getValue().getStatus()),
                () -> assertEquals("Stripe exception", paymentEntityArgumentCaptor.getValue().getPaymentProviderMessage())
        );
    }

    @Test
    @DisplayName("Test cancel payment")
    void testCancelPaymentWhenOk() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentEntity));
        when(repository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        //act
        PaymentEntity result = service.cancelPayment(paymentEntity.getId());
        //assert
        ArgumentCaptor<PaymentEntity> paymentEntityArgumentCaptor = ArgumentCaptor.forClass(PaymentEntity.class);
        verify(repository).save(paymentEntityArgumentCaptor.capture());
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(PaymentStatus.CANCELLED, paymentEntityArgumentCaptor.getValue().getStatus()),
                () -> assertEquals("Status: cancelled", paymentEntityArgumentCaptor.getValue().getPaymentProviderMessage())
        );
    }

    @Test
    @DisplayName("Test cancel payment when payment is not found")
    void testCancelPaymentWhenPaymentNotFound() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        long paymentId = paymentEntity.getId();
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.cancelPayment(paymentId));
        assertThat(ex.getMessage(), is("Payment with id " + paymentId + " not found"));
    }

    @Test
    @DisplayName("Test cancel payment when payment is not in status PROCESSING")
    void testCancelPaymentWhenPaymentIsNotProcessing() {
        //arrange
        PaymentEntity paymentEntity = factory.createWithUserId("userId");
        paymentEntity.setStatus(PaymentStatus.SUCCESS);
        long paymentId = paymentEntity.getId();
        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentEntity));
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> service.cancelPayment(paymentId));
        assertThat(ex.getMessage(), is("Payment is not processing"));
    }

    @Test
    @DisplayName("Test create payment")
    void testCreatePaymentWhenOk() {
        //arrange
        PaymentEntity paymentEntity = factory.create();
        when(repository.save(any(PaymentEntity.class))).thenReturn(paymentEntity);
        //act
        PaymentEntity result = service.create(paymentEntity);
        //assert
        assertThat(result, equalTo(paymentEntity));
    }

    @Test
    @DisplayName("Test update payment")
    void testUpdatePaymentWhenOk() {
        //arrange
        PaymentEntity paymentEntity = factory.create();
        PaymentEntity paymentEntityUpdated = factory.create();
        when(repository.save(any(PaymentEntity.class))).thenReturn(paymentEntityUpdated);
        //act
        PaymentEntity result = service.update(paymentEntity.getId(), paymentEntity);
        //assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(paymentEntityUpdated, result)
        );
    }

    @Test
    @DisplayName("Test update payment when payment is null")
    void testUpdatePaymentWhenPaymentNotFound() {
        //arrange
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.update(1L, null));
        assertThat(ex.getMessage(), is("Entity not found"));
    }

    @Test
    @DisplayName("Test get payment by id")
    void testGetPaymentByIdWhenOk() {
        //arrange
        PaymentEntity paymentEntity = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentEntity));
        //act
        PaymentEntity result = service.getOneById(paymentEntity.getId());
        //assert
        assertThat(result, equalTo(paymentEntity));
    }

    @Test
    @DisplayName("Test get payment by id when payment is not found")
    void testGetPaymentByIdWhenPaymentNotFound() {
        //arrange
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.getOneById(1L));
        assertThat(ex.getMessage(), is("Payment with id 1 not found"));
    }

    @Test
    @DisplayName("Test get all payments")
    void testGetAllPaymentsWhenOk() {
        //arrange
        List<PaymentEntity> paymentEntities = factory.createList(50);
        when(repository.findAll()).thenReturn(paymentEntities);
        //act
        List<PaymentEntity> result = service.getAll();
        //assert
        assertThat(result, equalTo(paymentEntities));
    }

    @Test
    @DisplayName("Test getting a list of Payments paginated and sorted")
    void testGetPaymentsPaginatedAndSortedWhenOk() {
        //arrange
        var payments = factory.createList(50);
        when(repository.findAll(any(Pageable.class))).thenReturn(
                new PageImpl<>(payments, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS)
        );
        //act
        var result = service.getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertEquals(payments, result.getContent()),
                () -> assertEquals(DEFAULT_FIRST_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, result.getTotalElements()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getSort().getOrderFor("id").getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getSort().getOrderFor("id").getDirection().name())
        );
        verify(repository).findAll(DEFAULT_PAGEABLE_AT_FIRST_PAGE);
    }

    @Test
    @DisplayName("Test get all payments when no payments")
    void testGetAllPaymentsWhenNoPayments() {
        //arrange
        List<PaymentEntity> paymentEntities = new ArrayList<>();
        when(repository.findAll()).thenReturn(paymentEntities);
        //act
        List<PaymentEntity> result = service.getAll();
        //assert
        assertThat(result, equalTo(paymentEntities));
    }

    @Test
    @DisplayName("Test get all payments by user id paginated and sorted")
    void testGetAllPaymentsByUserIdPaginatedAndSortedWhenOk() {
        //arrange
        List<PaymentEntity> paymentEntities = factory.createList(50);
        when(repository.findByUserId(anyString(), any(Pageable.class))).thenReturn(
                new PageImpl<>(paymentEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS));
        //act
        Page<PaymentEntity> result = service.getAllPaginatedAndSortedByUserId("userId", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(paymentEntities, result.getContent()),
                () -> assertEquals(DEFAULT_FIRST_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getPageable().getSort().getOrderFor(DEFAULT_SORT_BY).getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getPageable().getSort().getOrderFor(DEFAULT_SORT_BY).getDirection().name()),
                () -> assertEquals(DEFAULT_TOTAL_ELEMENTS, result.getTotalElements())
        );
    }

    @Test
    @DisplayName("Test get all payments by user id paginated and sorted when no payments")
    void testGetAllPaymentsByUserIdPaginatedAndSortedWhenNoPayments() {
        //arrange
        List<PaymentEntity> paymentEntities = new ArrayList<>();
        when(repository.findByUserId(anyString(), any(Pageable.class))).thenReturn(
                new PageImpl<>(paymentEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, ZERO_TOTAL_ELEMENTS));
        //act
        Page<PaymentEntity> result = service.getAllPaginatedAndSortedByUserId("userId", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(paymentEntities, result.getContent()),
                () -> assertEquals(DEFAULT_FIRST_PAGE, result.getPageable().getPageNumber()),
                () -> assertEquals(DEFAULT_SIZE, result.getPageable().getPageSize()),
                () -> assertEquals(DEFAULT_SORT_BY, result.getPageable().getSort().getOrderFor(DEFAULT_SORT_BY).getProperty()),
                () -> assertEquals(DEFAULT_SORT_ORDER, result.getPageable().getSort().getOrderFor(DEFAULT_SORT_BY).getDirection().name()),
                () -> assertEquals(ZERO_TOTAL_ELEMENTS, result.getTotalElements())
        );
    }

    @Test
    @DisplayName("Test delete payment by id")
    void testDeletePaymentByIdWhenOk() {
        //arrange
        PaymentEntity paymentEntity = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(paymentEntity));
        //act
        boolean result = service.deleteById(paymentEntity.getId());
        //assert
        assertAll(
                () -> assertTrue(result),
                () -> verify(repository, times(1)).delete(paymentEntity)
        );
    }

    @Test
    @DisplayName("Test deleting a payment when delete throws an exception")
    void testDeletePaymentWhenDeleteThrowsException() {
        //arrange
        var payment = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(payment));
        doThrow(new RuntimeException("")).when(repository).delete(payment);
        //act
        boolean result = service.deleteById(1L);
        //assert
        assertThat(result, is(false));
        verify(repository).findById(1L);
        verify(repository).delete(payment);
    }

    @Test
    @DisplayName("Test delete payment by id when payment is not found")
    void testDeletePaymentByIdWhenPaymentNotFound() {
        //arrange
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.deleteById(1L));
        assertThat(ex.getMessage(), is("Payment with id 1 not found"));
    }

}