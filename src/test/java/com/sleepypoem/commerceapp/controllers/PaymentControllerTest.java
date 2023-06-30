package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.PaymentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.domain.enums.Currency;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.domain.mappers.PaymentMapper;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import com.sleepypoem.commerceapp.services.PaymentService;
import com.sleepypoem.commerceapp.utils.factories.impl.PaymentFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static com.sleepypoem.commerceapp.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    PaymentController controller;

    @Mock
    PaymentService service;

    PaymentMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PaymentMapper();
        controller = new PaymentController(service, mapper);
    }

    @Test
    @DisplayName("Test start processing payment when ok")
    void testStartProcessingPaymentWhenOk() {
        //arrange
        PaymentEntity payment = new PaymentFactory().create();
        PaymentRequestDto paymentRequest = new PaymentRequestDto();
        paymentRequest.setUserId("userId");
        paymentRequest.setCheckout(payment.getCheckout());
        paymentRequest.setCurrency(Currency.USD);
        String message = "Created payment with id " + payment.getId();
        String url = "GET : /api/payments/" + payment.getId();
        when(service.startPayment(any(PaymentRequestDto.class))).thenReturn(payment);
        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.processPayment(paymentRequest);
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getMessage(), is(message));
        assertThat(response.getBody().getUrl(), is(url));
        verify(service).startPayment(paymentRequest);
    }

    @Test
    @DisplayName("Test confirm payment when ok")
    void testConfirmPaymentWhenOk() {
        //arrange
        PaymentEntity payment = new PaymentFactory().create();
        String paymentProviderMessage = "test error message";
        payment.setPaymentProviderMessage(paymentProviderMessage);
        payment.setStatus(PaymentStatus.SUCCESS);
        String message = "Payment with id " + payment.getId() + " was confirmed. Message: " + paymentProviderMessage;
        String url = "GET : /api/payments/" + payment.getId();
        when(service.confirmPayment(any(Long.class))).thenReturn(payment);
        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.updatePaymentStatus(payment.getId(), "confirm");
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getMessage(), is(message));
        assertThat(response.getBody().getUrl(), is(url));
        verify(service).confirmPayment(payment.getId());
    }

    @Test
    @DisplayName("Test confirm payment when payment fails")
    void testConfirmPaymentWhenPaymentFails() {
        //arrange
        PaymentEntity payment = new PaymentFactory().create();
        String paymentProviderMessage = "test error message";
        payment.setPaymentProviderMessage(paymentProviderMessage);
        payment.setStatus(PaymentStatus.FAILED);
        String message = "Payment with id " + payment.getId() + " failed, check card details and try again. Message: " + paymentProviderMessage;
        String url = "GET : /api/payments/" + payment.getId();
        when(service.confirmPayment(any(Long.class))).thenReturn(payment);
        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.updatePaymentStatus(payment.getId(), "confirm");
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getMessage(), is(message));
        assertThat(response.getBody().getUrl(), is(url));
        verify(service).confirmPayment(payment.getId());
    }

    @Test
    @DisplayName("Test cancel payment when ok")
    void testCancelPaymentWhenOk() {
        //arrange
        PaymentEntity payment = new PaymentFactory().create();
        String paymentProviderMessage = "test error message";
        payment.setPaymentProviderMessage(paymentProviderMessage);
        payment.setStatus(PaymentStatus.CANCELLED);
        String message = "Payment with id " + payment.getId() + " was cancelled. Message: " + paymentProviderMessage;
        String url = "GET : /api/payments/" + payment.getId();
        when(service.cancelPayment(any(Long.class))).thenReturn(payment);
        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.updatePaymentStatus(payment.getId(), "cancel");
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getMessage(), is(message));
        assertThat(response.getBody().getUrl(), is(url));
        verify(service).cancelPayment(payment.getId());
    }

    @Test
    @DisplayName("Test update payment status when action is not valid")
    void testUpdatePaymentStatusWhenActionNotValid() {
        //arrange
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> controller.updatePaymentStatus(1L, "invalidAction"));
        assertThat(ex.getMessage(), is("Invalid action, must be cancel or confirm"));
    }

    @Test
    @DisplayName("Test update payment status when unexpected status")
    void testUpdatePaymentStatusWhenUnexpectedStatus() {
        //arrange
        PaymentEntity payment = new PaymentFactory().create();
        payment.setStatus(PaymentStatus.PENDING);
        when(service.cancelPayment(any(Long.class))).thenReturn(payment);
        //act
        //assert
        var ex = assertThrows(MyInternalException.class, () -> controller.updatePaymentStatus(1L, "cancel"));
        assertThat(ex.getMessage(), is("Unexpected value: PENDING"));
    }

    @Test
    @DisplayName("Test get payment by id when ok")
    void testGetPaymentByIdWhenOk() {
        //arrange
        PaymentEntity payment = new PaymentFactory().create();
        PaymentDto paymentDto = mapper.convertToDto(payment);
        when(service.getOneById(anyLong())).thenReturn(payment);
        //act
        ResponseEntity<PaymentDto> response = controller.findOneById(payment.getId());
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(paymentDto));
        verify(service).getOneById(payment.getId());
    }

    @Test
    @DisplayName("Test get payments by user id at first page when ok")
    void getPaymentsByUserIdAtFirstPageWhenOk() {
        List<PaymentEntity> paymentEntities = new PaymentFactory().createList(50);
        List<PaymentDto> paymentDtos = mapper.convertToDtoList(paymentEntities);
        String nextPageUrl = "/api/payments?user-id=1&page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<PaymentEntity> page = new PageImpl<>(paymentEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSortedByUserId(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        ResponseEntity<PaginatedDto<PaymentDto>> responseEntity = controller.getByUserIdPaginatedAndSorted("1", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(paymentDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
    }

    @Test
    @DisplayName("Test get payments by user id at last page when ok")
    void getPaymentsByUserIdAtLastPageWhenOk() {
        List<PaymentEntity> paymentEntities = new PaymentFactory().createList(50);
        List<PaymentDto> paymentDtos = mapper.convertToDtoList(paymentEntities);
        String previousPageUrl = "/api/payments?user-id=1&page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<PaymentEntity> page = new PageImpl<>(paymentEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSortedByUserId(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        ResponseEntity<PaginatedDto<PaymentDto>> responseEntity = controller.getByUserIdPaginatedAndSorted("1", DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(paymentDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
    }

    @Test
    @DisplayName("Test get all payments when ok")
    void testGetAllPaymentsWhenOk() {
        //arrange
        List<PaymentEntity> paymentEntities = new PaymentFactory().createList(50);
        List<PaymentDto> paymentDtos = mapper.convertToDtoList(paymentEntities);
        when(service.getAll()).thenReturn(paymentEntities);
        //act
        ResponseEntity<Iterable<PaymentDto>> response = controller.findAll();
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(paymentDtos));
        verify(service).getAll();
    }

    @Test
    @DisplayName("Test get all payments paginated and sorted at first page when ok")
    void testGetAllPaymentsPaginatedAndSortedAtFirstPageWhenOk() {
        //arrange
        List<PaymentEntity> paymentEntities = new PaymentFactory().createList(50);
        List<PaymentDto> paymentDtos = mapper.convertToDtoList(paymentEntities);
        String nextPageUrl = "/api/payments?page=1&size=10&sortBy=id&sortOrder=ASC";
        Page<PaymentEntity> page = new PageImpl<>(paymentEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);
        //act
        ResponseEntity<PaginatedDto<PaymentDto>> response = controller.getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(response.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(response.getBody().getContent(), is(paymentDtos));
        assertThat(response.getBody().getNextPage(), is(nextPageUrl));
        assertThat(response.getBody().getPreviousPage(), is(nullValue()));
        verify(service).getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
    }

    @Test
    @DisplayName("Test get all payments paginated and sorted at last page when ok")
    void testGetAllPaymentsPaginatedAndSortedAtLastPageWhenOk() {
        //arrange
        List<PaymentEntity> paymentEntities = new PaymentFactory().createList(50);
        List<PaymentDto> paymentDtos = mapper.convertToDtoList(paymentEntities);
        String previousPageUrl = "/api/payments?page=3&size=10&sortBy=id&sortOrder=ASC";
        Page<PaymentEntity> page = new PageImpl<>(paymentEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);
        //act
        ResponseEntity<PaginatedDto<PaymentDto>> response = controller.getAllPaginatedAndSorted(DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(response.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(response.getBody().getContent(), is(paymentDtos));
        assertThat(response.getBody().getNextPage(), is(nullValue()));
        assertThat(response.getBody().getPreviousPage(), is(previousPageUrl));
        verify(service).getAllPaginatedAndSorted(DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
    }

}