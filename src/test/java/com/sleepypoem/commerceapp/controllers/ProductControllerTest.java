package com.sleepypoem.commerceapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.ProductDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.domain.mappers.ProductMapper;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.utils.factories.impl.ProductFactory;
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
class ProductControllerTest {

    ProductController controller;

    @Mock
    ProductService service;

    ProductMapper mapper;

    @BeforeEach
    void setUp() {
        this.mapper = new ProductMapper();
        this.controller = new ProductController(service, mapper);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    @DisplayName("Test create product when ok")
    @SuppressWarnings("all")
    void createProductWhenOk() {
        //arrange
        ProductEntity productEntity = new ProductFactory().create();
        ProductDto productDto = mapper.convertToDto(productEntity);
        String message = "Product created with id " + productEntity.getId();
        String url = "GET : /api/products/" + productEntity.getId();

        //act
        when(service.create(any(ProductEntity.class))).thenReturn(productEntity);
        ResponseEntity<ResourceStatusResponseDto> response = controller.create(productEntity);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(response.getBody().getMessage(), is(message));
        assertThat(response.getBody().getUrl(), is(url));
        verify(service).create(productEntity);

    }

    @Test
    @DisplayName("Test update product when ok")
    void updateProductWhenOk() {
        //arrange
        ProductEntity productEntity = new ProductFactory().create();
        ProductDto productDto = mapper.convertToDto(productEntity);
        Long id = productEntity.getId();
        when(service.update(anyLong(), any(ProductEntity.class))).thenReturn(productEntity);

        //act
        ResponseEntity<ProductDto> response = controller.update(id, productEntity);
        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is(productDto));
    }

    @Test
    @DisplayName("Test delete product when ok")
    void deleteProductWhenOk() {
        //arrange
        ProductEntity productEntity = new ProductFactory().create();
        Long id = productEntity.getId();
        String message = "Product with id " + id + " deleted";
        when(service.deleteById(anyLong())).thenReturn(true);

        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.delete(id);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody().getMessage(), is(message));
    }

    @Test
    @DisplayName("Test delete product when error")
    void deleteProductWhenError() {
        //arrange
        ProductEntity productEntity = new ProductFactory().create();
        Long id = productEntity.getId();
        String message = "Error deleting product with id " + id;
        when(service.deleteById(anyLong())).thenReturn(false);

        //act
        ResponseEntity<ResourceStatusResponseDto> response = controller.delete(id);

        //assert
        assertThat(response.getStatusCode(), is(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(response.getBody().getMessage(), is(message));
    }

    @Test
    @DisplayName("Test get all products paginated and sorted at first page when ok")
    void getAllProductsPaginatedAndSortedWhenOk() {
        //arrange
        List<ProductEntity> productEntities = new ProductFactory().createList(50);
        List<ProductDto> productDtos = mapper.convertToDtoList(productEntities);
        String nextPageUrl = "/api/products?page=1&size=10&sortBy=id&sortOrder=ASC";

        Page<ProductEntity> page = new PageImpl<>(productEntities, DEFAULT_PAGEABLE_AT_FIRST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        //act
        ResponseEntity<PaginatedDto<ProductDto>> responseEntity = controller.getAllPaginatedAndSorted(DEFAULT_FIRST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_FIRST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(productDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nextPageUrl));
        assertThat(responseEntity.getBody().getPreviousPage(), is(nullValue()));
    }

    @Test
    @DisplayName("Test get all products paginated and sorted at last page when ok")
    void getAllProductsPaginatedAndSortedAtLastPageWhenOk() {
        //arrange
        List<ProductEntity> productEntities = new ProductFactory().createList(50);
        List<ProductDto> productDtos = mapper.convertToDtoList(productEntities);
        String previousPageUrl = "/api/products?page=3&size=10&sortBy=id&sortOrder=ASC";

        Page<ProductEntity> page = new PageImpl<>(productEntities, DEFAULT_PAGEABLE_AT_LAST_PAGE, DEFAULT_TOTAL_ELEMENTS);
        when(service.getAllPaginatedAndSorted(anyInt(), anyInt(), anyString(), anyString())).thenReturn(page);

        //act
        ResponseEntity<PaginatedDto<ProductDto>> responseEntity = controller.getAllPaginatedAndSorted(DEFAULT_LAST_PAGE, DEFAULT_SIZE, DEFAULT_SORT_BY, DEFAULT_SORT_ORDER);

        //assert
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertThat(responseEntity.getBody().getCurrentPage(), is(DEFAULT_LAST_PAGE));
        assertThat(responseEntity.getBody().getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS));
        assertThat(responseEntity.getBody().getContent(), is(productDtos));
        assertThat(responseEntity.getBody().getNextPage(), is(nullValue()));
        assertThat(responseEntity.getBody().getPreviousPage(), is(previousPageUrl));
    }


    @Test
    @DisplayName("Test get product by id when ok")
    void getProductByIdWhenOk() {
        //arrange
        ProductEntity productEntity = new ProductFactory().create();
        ProductDto productDto = mapper.convertToDto(productEntity);

        when(service.getOneById(anyLong())).thenReturn(productEntity);

        //act
        ResponseEntity<ProductDto> responseEntity = controller.getOneById(1L);

        //assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(productDto, responseEntity.getBody());
    }

    @Test
    @DisplayName("Test get product by id when product not found")
    void getProductByIdWhenProductNotFound() {
        //arrange
        when(service.getOneById(anyLong())).thenThrow(new MyEntityNotFoundException("Product not found"));
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> controller.getOneById(1L));

        assertEquals("Product not found", ex.getMessage());
    }

}