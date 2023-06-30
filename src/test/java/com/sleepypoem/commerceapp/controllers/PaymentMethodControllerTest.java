package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.domain.dto.CardDto;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.PaymentMethodMapper;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.services.PaymentMethodService;
import com.sleepypoem.commerceapp.utils.factories.impl.PaymentMethodFactory;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentMethodControllerTest {

    PaymentMethodController controller;

    @Mock
    PaymentMethodService service;

    PaymentMethodMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PaymentMethodMapper();
        controller = new PaymentMethodController(mapper, service);
    }

    @Test
    @DisplayName("Test create paymentMethod when ok")
    @SuppressWarnings("all")
    void createPaymentMethodWhenOk() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodFactory().create();
        PaymentMethodDto paymentMethodDto = mapper.convertToDto(paymentMethodEntity);
        String message = "Payment method created with id " + paymentMethodEntity.getId();
        String url = "GET : /api/payment-methods/" + paymentMethodEntity.getId();
        CardDto cardDto = new CardDto();
        cardDto.setCardToken("cardToken");
        cardDto.setUserId("userId");

        //act
        when(service.createCard(anyString(), anyString())).thenReturn(paymentMethodEntity);
        ResponseEntity<ResourceStatusResponseDto> response = controller.create(cardDto);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getMessage(), is(message));
        assertThat(response.getBody().getUrl(), is(url));
        verify(service).createCard(cardDto.getCardToken(), cardDto.getUserId());

    }

    @Test
    @DisplayName("Test update paymentMethod when ok")
    void updatePaymentMethodWhenOk() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodFactory().create();
        PaymentMethodDto paymentMethodDto = mapper.convertToDto(paymentMethodEntity);
        Long id = paymentMethodEntity.getId();
        CardDto cardDto = new CardDto();
        cardDto.setCardToken("cardToken");
        cardDto.setUserId("userId");
        when(service.updateCard(anyLong(), anyString())).thenReturn(paymentMethodEntity);

        //act
        ResponseEntity<PaymentMethodDto> response = controller.update(id, cardDto);
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(paymentMethodDto));
        verify(service).updateCard(id, cardDto.getCardToken());
    }

    @Test
    @DisplayName("Test delete paymentMethod when ok")
    void deletePaymentMethodWhenOk() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodFactory().create();
        Long id = paymentMethodEntity.getId();
        String message = "Payment method with id " + id + " deleted";
        when(service.deleteById(anyLong())).thenReturn(true);

        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.delete(id);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getMessage(), is(message));
        verify(service).deleteById(id);
    }

    @Test
    @DisplayName("Test delete paymentMethod when error")
    void deletePaymentMethodWhenError() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodFactory().create();
        Long id = paymentMethodEntity.getId();
        String message = "Error deleting payment method with id " + id;
        when(service.deleteById(anyLong())).thenReturn(false);

        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.delete(id);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody().getMessage(), is(message));
        verify(service).deleteById(id);
    }

    @Test
    @DisplayName("Test get paymentMethods by user id at first page when ok")
    void getPaymentMethodByUserIdAtFirstPageWhenOk() {

        //arrange
        List<PaymentMethodEntity> paymentMethodEntities = new PaymentMethodFactory().createList(50);
        List<PaymentMethodDto> paymentMethodDtos = mapper.convertToDtoList(paymentMethodEntities);
        String nextPageUrl = "/api/payment-methods?user-id=1&page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<PaymentMethodEntity> page = new PageImpl<>(paymentMethodEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSortedByUserId(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);


        //act
        ResponseEntity<PaginatedDto<PaymentMethodDto>> responseEntity = controller.getByUserIdPaginatedAndSorted("1", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(paymentMethodDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
        verify(service).getAllPaginatedAndSortedByUserId("1", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
    }

    @Test
    @DisplayName("Test get paymentMethods by user id at last page when ok")
    void getPaymentMethodByUserIdAtLastPageWhenOk() {
        //arrange
        List<PaymentMethodEntity> paymentMethodEntities = new PaymentMethodFactory().createList(50);
        List<PaymentMethodDto> paymentMethodDtos = mapper.convertToDtoList(paymentMethodEntities);
        String previousPageUrl = "/api/payment-methods?user-id=1&page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<PaymentMethodEntity> page = new PageImpl<>(paymentMethodEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSortedByUserId(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        //act
        ResponseEntity<PaginatedDto<PaymentMethodDto>> responseEntity = controller.getByUserIdPaginatedAndSorted("1", DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(paymentMethodDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
        verify(service).getAllPaginatedAndSortedByUserId("1", DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
    }

    @Test
    @DisplayName("Test get all paymentMethods paginated and sorted at first page when ok")
    void getAllPaymentMethodsPaginatedAndSortedWhenOk() {
        //arrange
        List<PaymentMethodEntity> paymentMethodEntities = new PaymentMethodFactory().createList(50);
        List<PaymentMethodDto> paymentMethodDtos = mapper.convertToDtoList(paymentMethodEntities);
        String nextPageUrl = "/api/payment-methods?page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<PaymentMethodEntity> page = new PageImpl<>(paymentMethodEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        //act
        ResponseEntity<PaginatedDto<PaymentMethodDto>> responseEntity = controller.getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(paymentMethodDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
        verify(service).getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
    }

    @Test
    @DisplayName("Test get all paymentMethods paginated and sorted at last page when ok")
    void getAllPaymentMethodsPaginatedAndSortedAtLastPageWhenOk() {
        //arrange
        List<PaymentMethodEntity> paymentMethodEntities = new PaymentMethodFactory().createList(50);
        List<PaymentMethodDto> paymentMethodDtos = mapper.convertToDtoList(paymentMethodEntities);
        String previousPageUrl = "/api/payment-methods?page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<PaymentMethodEntity> page = new PageImpl<>(paymentMethodEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        //act
        ResponseEntity<PaginatedDto<PaymentMethodDto>> responseEntity = controller.getAllPaginatedAndSorted(DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(paymentMethodDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
        verify(service).getAllPaginatedAndSorted(DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
    }


    @Test
    @DisplayName("Test get paymentMethod by id when ok")
    void getPaymentMethodByIdWhenOk() {
        //arrange
        PaymentMethodEntity paymentMethodEntity = new PaymentMethodFactory().create();
        PaymentMethodDto paymentMethodDto = mapper.convertToDto(paymentMethodEntity);

        when(service.getOneById(anyLong())).thenReturn(paymentMethodEntity);

        //act
        ResponseEntity<PaymentMethodDto> responseEntity = controller.getOneById(1L);

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(paymentMethodDto, responseEntity.getBody());
        verify(service).getOneById(1L);
    }

    @Test
    @DisplayName("Test get payment method by id when paymentMethod not found")
    void getPaymentMethodByIdWhenPaymentMethodNotFound() {
        //arrange
        when(service.getOneById(anyLong())).thenThrow(new MyEntityNotFoundException("PaymentMethod not found"));
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> controller.getOneById(1L));

        assertEquals("PaymentMethod not found", ex.getMessage());
        verify(service).getOneById(1L);
    }

}