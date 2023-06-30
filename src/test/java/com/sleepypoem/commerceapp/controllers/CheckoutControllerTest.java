package com.sleepypoem.commerceapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sleepypoem.commerceapp.controllers.utils.SecurityUtils;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.entities.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutItemMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutMapper;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.services.CheckoutService;
import com.sleepypoem.commerceapp.utils.factories.impl.CheckoutFactory;
import com.sleepypoem.commerceapp.utils.factories.impl.CheckoutItemFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static com.sleepypoem.commerceapp.utils.TestConstants.*;
import static com.sleepypoem.commerceapp.utils.TestConstants.DEFAULT_TOTAL_ELEMENTS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutControllerTest {


    CheckoutController controller;
    CheckoutMapper mapper;

    @Mock
    private CheckoutService service;

    MockedStatic<SecurityUtils> mockSecurityUtils;


    @BeforeEach
    void setUp() {
        this.mapper = new CheckoutMapper();
        this.controller = new CheckoutController(mapper, service);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        mockSecurityUtils = Mockito.mockStatic(SecurityUtils.class);
    }

    @AfterEach
    void tearDown() {
        mockSecurityUtils.close();
    }

    @Test
    @DisplayName("Test create checkout when ok")
    @SuppressWarnings("all")
    void testGreateCheckoutWhenOk() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        CheckoutDto checkoutDto = mapper.convertToDto(checkoutEntity);
        String message = "Checkout created with id " + checkoutEntity.getId();
        String url = "GET : /api/checkouts/" + checkoutEntity.getId();
        mockSecurityUtils.when(SecurityUtils::getCurrentLoggedUserId).thenReturn("admin");

        when(service.create(any(CheckoutEntity.class))).thenReturn(checkoutEntity);
        ResponseEntity<ResourceStatusResponseDto> response = controller.create(checkoutEntity);

        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getMessage(), is(message));
        assertThat(response.getBody().getUrl(), is(url));

    }

    @Test
    @DisplayName("Test update checkout when ok")
    void testUpdateCheckoutWhenOk() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        CheckoutDto checkoutDto = mapper.convertToDto(checkoutEntity);
        Long id = checkoutEntity.getId();
        mockSecurityUtils.when(SecurityUtils::getCurrentLoggedUserId).thenReturn("admin");

        when(service.update(anyLong(), any(CheckoutEntity.class))).thenReturn(checkoutEntity);
        ResponseEntity<CheckoutDto> response = controller.update(id, checkoutEntity);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(checkoutDto));
    }

    @Test
    @DisplayName("Test delete checkout when ok")
    void testDeleteCheckoutWhenOk() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        Long id = checkoutEntity.getId();
        String message = "Checkout with id " + id + " deleted successfully";
        mockSecurityUtils.when(SecurityUtils::getCurrentLoggedUserId).thenReturn("admin");
        when(service.deleteById(anyLong())).thenReturn(true);
        ResponseEntity<ResourceStatusResponseDto> response = controller.delete(id);

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getMessage(), is(message));
    }

    @Test
    @DisplayName("Test delete checkout when error")
    void testDeleteCheckoutWhenError() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        Long id = checkoutEntity.getId();
        String message = "Error deleting checkout with id " + id;
        when(service.deleteById(anyLong())).thenReturn(false);
        ResponseEntity<ResourceStatusResponseDto> response = controller.delete(id);

        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody().getMessage(), is(message));
    }

    @Test
    @DisplayName("Test get checkouts by user id at first page when ok")
    void testGetCheckoutByUserIdAtFirstPageWhenOk() {
        List<CheckoutEntity> checkoutEntities = new CheckoutFactory().createList(50);
        List<CheckoutDto> checkoutDtos = mapper.convertToDtoList(checkoutEntities);
        String nextPageUrl = "/api/checkouts?userId=1&page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<CheckoutEntity> page = new PageImpl<>(checkoutEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSortedByUserId(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        ResponseEntity<PaginatedDto<CheckoutDto>> responseEntity = controller.getByUserIdPaginatedAndSorted("1", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(checkoutDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
    }

    @Test
    @DisplayName("Test get checkouts by user id at last page when ok")
    void testGetCheckoutByUserIdAtLastPageWhenOk() {
        List<CheckoutEntity> checkoutEntities = new CheckoutFactory().createList(50);
        List<CheckoutDto> checkoutDtos = mapper.convertToDtoList(checkoutEntities);
        String previousPageUrl = "/api/checkouts?userId=1&page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<CheckoutEntity> page = new PageImpl<>(checkoutEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSortedByUserId(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        ResponseEntity<PaginatedDto<CheckoutDto>> responseEntity = controller.getByUserIdPaginatedAndSorted("1", DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(checkoutDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
    }

    @Test
    @DisplayName("Test get all checkouts paginated and sorted at first page when ok")
    void testGetAllCheckoutsPaginatedAndSortedWhenOk() {
        List<CheckoutEntity> checkoutEntities = new CheckoutFactory().createList(50);
        List<CheckoutDto> checkoutDtos = mapper.convertToDtoList(checkoutEntities);
        String nextPageUrl = "/api/checkouts?page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<CheckoutEntity> page = new PageImpl<>(checkoutEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        ResponseEntity<PaginatedDto<CheckoutDto>> responseEntity = controller.getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(checkoutDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
    }

    @Test
    @DisplayName("Test get all checkouts paginated and sorted at last page when ok")
    void testGetAllCheckoutsPaginatedAndSortedAtLastPageWhenOk() {
        List<CheckoutEntity> checkoutEntities = new CheckoutFactory().createList(50);
        List<CheckoutDto> checkoutDtos = mapper.convertToDtoList(checkoutEntities);
        String previousPageUrl = "/api/checkouts?page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<CheckoutEntity> page = new PageImpl<>(checkoutEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        ResponseEntity<PaginatedDto<CheckoutDto>> responseEntity = controller.getAllPaginatedAndSorted(DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(checkoutDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
    }


    @Test
    @DisplayName("Test get checkout by id when ok")
    void testGetCheckoutByIdWhenOk() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        CheckoutDto checkoutDto = mapper.convertToDto(checkoutEntity);

        when(service.getOneById(anyLong())).thenReturn(checkoutEntity);

        ResponseEntity<CheckoutDto> responseEntity = controller.getOneById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(checkoutDto, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test get checkout by id when checkout not found")
    void testGetCheckoutByIdWhenCheckoutNotFound() {

        when(service.getOneById(anyLong())).thenThrow(new MyEntityNotFoundException("Checkout not found"));

        var ex = assertThrows(MyEntityNotFoundException.class, () -> controller.getOneById(1L));

        assertEquals("Checkout not found", ex.getMessage());
    }

    @Test
    @DisplayName("Test add an item to checkout when ok")
    void testAddItemToCheckoutWhenOk() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        CheckoutDto checkoutDto = mapper.convertToDto(checkoutEntity);
        List<CheckoutItemEntity> checkoutItemEntities = new CheckoutItemFactory().createList(1);
        checkoutDto.setItems(new CheckoutItemMapper().convertToDtoList(checkoutItemEntities));

        when(service.addItems(anyLong(), any())).thenReturn(checkoutEntity);

        ResponseEntity<CheckoutDto> responseEntity = controller.addItemsToCart(1L, checkoutItemEntities);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(checkoutDto, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test add an item to checkout when item list is empty")
    void testAddItemToCheckoutWhenItemListIsEmpty() {
        var emptyList = new ArrayList<CheckoutItemEntity>();
        var ex = assertThrows(MyBadRequestException.class, () -> controller.addItemsToCart(1L, emptyList));
        assertThat(ex.getMessage(), is("Items list cannot be empty"));
    }

    @Test
    @DisplayName("Test remove an item from checkout when ok")
    void testRemoveItemFromCheckoutWhenOk() {

       doNothing().when(service).removeItem(anyLong(), anyLong());

        ResponseEntity<Void> responseEntity = controller.removeItemFromCart(1L, 1L);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(service, times(1)).removeItem(1L, 1L);
    }

    @Test
    @DisplayName("Test modify quantity of an item in checkout when ok")
    void testModifyQuantityOfItemInCheckoutWhenOk() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        CheckoutDto checkoutDto = mapper.convertToDto(checkoutEntity);

        doNothing().when(service).modifyItemQuantity(anyLong(), anyLong(), anyInt());
        when(service.getOneById(anyLong())).thenReturn(checkoutEntity);

        ResponseEntity<CheckoutDto> responseEntity = controller.modifyQuantity(1L, 1L, 1);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(checkoutDto, responseEntity.getBody());
        verify(service).modifyItemQuantity(1L, 1L, 1);
        verify(service).getOneById(1L);
    }

    @Test
    @DisplayName("Test get all items from a checkout paginated and sorted at first page when ok")
    void testGetAllItemsFromCheckoutPaginatedAndSortedAtFirstPageWhenOk() {
        List<CheckoutItemEntity> checkoutItemEntities = new CheckoutItemFactory().createList(50);
        List<CheckoutItemDto> checkoutItemDtos = new CheckoutItemMapper().convertToDtoList(checkoutItemEntities);
        String nextPageUrl = "/api/checkouts/1/items?page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<CheckoutItemEntity> page = new PageImpl<>(checkoutItemEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllItemsPaginatedAndSorted(anyLong(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        ResponseEntity<PaginatedDto<CheckoutItemDto>> responseEntity = controller.getItemsPaginatedAndSorted(1L, DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(checkoutItemDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
    }

    @Test
    @DisplayName("Test get all items from a checkout paginated and sorted at last page when ok")
    void testGetAllItemsFromCheckoutPaginatedAndSortedAtLastPageWhenOk() {
        List<CheckoutItemEntity> checkoutItemEntities = new CheckoutItemFactory().createList(50);
        List<CheckoutItemDto> checkoutItemDtos = new CheckoutItemMapper().convertToDtoList(checkoutItemEntities);
        String previousPageUrl = "/api/checkouts/1/items?page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<CheckoutItemEntity> page = new PageImpl<>(checkoutItemEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllItemsPaginatedAndSorted(anyLong(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        ResponseEntity<PaginatedDto<CheckoutItemDto>> responseEntity = controller.getItemsPaginatedAndSorted(1L, DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(checkoutItemDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
    }

    @Test
    @DisplayName("Test set preferred address when ok")
    void testSetPreferredAddressWhenOk() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        CheckoutDto checkoutDto = mapper.convertToDto(checkoutEntity);
        AddressEntity addressEntity = checkoutEntity.getAddress();

        when(service.addPreferredAddress(anyLong(), any())).thenReturn(checkoutEntity);

        ResponseEntity<CheckoutDto> responseEntity = controller.addPreferredAddress(1L, addressEntity);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(checkoutDto, responseEntity.getBody());
        verify(service).addPreferredAddress(1L, addressEntity);
    }

    @Test
    @DisplayName("Test set preferred payment method when ok")
    void testSetPreferredPaymentMethodWhenOk() {
        CheckoutEntity checkoutEntity = new CheckoutFactory().create();
        CheckoutDto checkoutDto = mapper.convertToDto(checkoutEntity);
        PaymentMethodEntity paymentMethodEntity = checkoutEntity.getPaymentMethod();

        when(service.addPreferredPaymentMethod(anyLong(), any())).thenReturn(checkoutEntity);

        ResponseEntity<CheckoutDto> responseEntity = controller.addPreferredPaymentMethod(1L, paymentMethodEntity);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(checkoutDto, responseEntity.getBody());
        verify(service).addPreferredPaymentMethod(1L, paymentMethodEntity);
    }
}