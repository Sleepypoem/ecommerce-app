package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.utils.SecurityUtils;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.AddressMapper;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.services.AddressService;
import com.sleepypoem.commerceapp.utils.factories.impl.AddressFactory;
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
import static com.sleepypoem.commerceapp.utils.TestConstants.*;

import java.util.List;

import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

    AddressController controller;
    AddressMapper mapper;

    @Mock
    private AddressService service;

    MockedStatic<SecurityUtils> mockSecurityUtils;


    @BeforeEach
    void setUp() {
        this.mapper = new AddressMapper();
        this.controller = new AddressController(mapper, service);
        mockSecurityUtils = Mockito.mockStatic(SecurityUtils.class);
    }

    @AfterEach
    void tearDown() {
        mockSecurityUtils.close();
    }

    @Test
    @DisplayName("Test create address when ok")
    @SuppressWarnings("all")
    void createAddressWhenOk() {
        //arrange
        AddressEntity addressEntity = new AddressFactory().create();
        AddressDto addressDto = mapper.convertToDto(addressEntity);
        String message = "Address created with id " + addressEntity.getId();
        String url = "GET : /api/addresses/" + addressEntity.getId();
        mockSecurityUtils.when(SecurityUtils::getCurrentLoggedUserId).thenReturn("admin");

        //act
        when(service.create(any(AddressEntity.class))).thenReturn(addressEntity);
        ResponseEntity<ResourceStatusResponseDto> response = controller.create(addressEntity);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getMessage(), is(message));
        assertThat(response.getBody().getUrl(), is(url));
        verify(service).create(addressEntity);

    }

    @Test
    @DisplayName("Test update address when ok")
    void updateAddressWhenOk() {
        //arrange
        AddressEntity addressEntity = new AddressFactory().create();
        AddressDto addressDto = mapper.convertToDto(addressEntity);
        Long id = addressEntity.getId();
        mockSecurityUtils.when(SecurityUtils::getCurrentLoggedUserId).thenReturn("admin");
        when(service.update(anyLong(), any(AddressEntity.class))).thenReturn(addressEntity);

        //act
        ResponseEntity<AddressDto> response = controller.update(id, addressEntity);
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(addressDto));
    }

    @Test
    @DisplayName("Test delete address when ok")
    void deleteAddressWhenOk() {
        //arrange
        AddressEntity addressEntity = new AddressFactory().create();
        Long id = addressEntity.getId();
        String message = "Address deleted with id " + id;
        mockSecurityUtils.when(SecurityUtils::getCurrentLoggedUserId).thenReturn("admin");
        when(service.deleteById(anyLong())).thenReturn(true);

        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.delete(id);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getMessage(), is(message));
    }

    @Test
    @DisplayName("Test delete address when error")
    void deleteAddressWhenError() {
        //arrange
        AddressEntity addressEntity = new AddressFactory().create();
        Long id = addressEntity.getId();
        String message = "Error deleting address with id " + id;
        mockSecurityUtils.when(SecurityUtils::getCurrentLoggedUserId).thenReturn("admin");
        when(service.deleteById(anyLong())).thenReturn(false);

        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.delete(id);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody().getMessage(), is(message));
    }

    @Test
    @DisplayName("Test get addresses by user id at first page when ok")
    void getAddressByUserIdAtFirstPageWhenOk() {

        //arrange
        List<AddressEntity> addressEntities = new AddressFactory().createList(50);
        List<AddressDto> addressDtos = mapper.convertToDtoList(addressEntities);
        String nextPageUrl = "/api/addresses?user-id=1&page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<AddressEntity> page = new PageImpl<>(addressEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.findByUserId(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);


        //act
        ResponseEntity<PaginatedDto<AddressDto>> responseEntity = controller.getByUserIdPaginatedAndSorted("1", DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(addressDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
    }

    @Test
    @DisplayName("Test get addresses by user id at last page when ok")
    void getAddressByUserIdAtLastPageWhenOk() {
        //arrange
        List<AddressEntity> addressEntities = new AddressFactory().createList(50);
        List<AddressDto> addressDtos = mapper.convertToDtoList(addressEntities);
        String previousPageUrl = "/api/addresses?user-id=1&page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<AddressEntity> page = new PageImpl<>(addressEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.findByUserId(anyString(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        //act
        ResponseEntity<PaginatedDto<AddressDto>> responseEntity = controller.getByUserIdPaginatedAndSorted("1", DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(addressDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
    }

    @Test
    @DisplayName("Test get all addresses paginated and sorted at first page when ok")
    void getAllAddressesPaginatedAndSortedWhenOk() {
        //arrange
        List<AddressEntity> addressEntities = new AddressFactory().createList(50);
        List<AddressDto> addressDtos = mapper.convertToDtoList(addressEntities);
        String nextPageUrl = "/api/addresses?page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<AddressEntity> page = new PageImpl<>(addressEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        //act
        ResponseEntity<PaginatedDto<AddressDto>> responseEntity = controller.getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(addressDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
    }

    @Test
    @DisplayName("Test get all addresses paginated and sorted at last page when ok")
    void getAllAddressesPaginatedAndSortedAtLastPageWhenOk() {
        //arrange
        List<AddressEntity> addressEntities = new AddressFactory().createList(50);
        List<AddressDto> addressDtos = mapper.convertToDtoList(addressEntities);
        String previousPageUrl = "/api/addresses?page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<AddressEntity> page = new PageImpl<>(addressEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        //act
        ResponseEntity<PaginatedDto<AddressDto>> responseEntity = controller.getAllPaginatedAndSorted(DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(addressDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
    }


    @Test
    @DisplayName("Test get address by id when ok")
    void getAddressByIdWhenOk() {
        //arrange
        AddressEntity addressEntity = new AddressFactory().create();
        AddressDto addressDto = mapper.convertToDto(addressEntity);

        when(service.getOneById(anyLong())).thenReturn(addressEntity);

        //act
        ResponseEntity<AddressDto> responseEntity = controller.getOneById(1L);

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(addressDto, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test get address by id when address not found")
    void getAddressByIdWhenAddressNotFound() {
        //arrange
        when(service.getOneById(anyLong())).thenThrow(new MyEntityNotFoundException("Address not found"));
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> controller.getOneById(1L));

        assertEquals("Address not found", ex.getMessage());
    }

}