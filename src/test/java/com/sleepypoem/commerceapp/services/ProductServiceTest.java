package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.ProductRepository;
import com.sleepypoem.commerceapp.utils.factories.impl.ProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.sleepypoem.commerceapp.utils.TestConstants.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    ProductService service;

    @Mock
    ProductRepository repository;

    ProductFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ProductFactory();
        service = new ProductService(repository);
    }

    @Test
    @DisplayName("Test creating a product")
    void testCreateProductWhenOk() {
        //arrange
        ProductEntity product = factory.create();
        when(repository.save(product)).thenReturn(product);
        //act
        ProductEntity createdProduct = service.create(product);
        //assert
        assertThat(createdProduct, equalTo(product));
        verify(repository).save(product);
    }

    @Test
    @DisplayName("Test updating a product")
    void testUpdateProduct() {
        //arrange
        ProductEntity product = factory.create();
        ProductEntity newProduct = factory.create();
        when(repository.save(product)).thenReturn(newProduct);
        //act
        ProductEntity updatedProduct = service.update(product.getId(), product);
        //assert
        assertAll(
                () -> assertEquals(newProduct, updatedProduct),
                () -> assertEquals(newProduct.getId(), updatedProduct.getId()),
                () -> assertEquals(newProduct.getStock(), updatedProduct.getStock()),
                () -> assertEquals(newProduct.getPrice(), updatedProduct.getPrice())
        );
        verify(repository).save(product);
    }

    @Test
    @DisplayName("Test finding a product by id")
    void testFindProductByIdWhenOk() {
        //arrange
        ProductEntity product = factory.create();
        when(repository.findById(anyLong())).thenReturn(Optional.of(product));
        //act
        ProductEntity foundProduct = service.getOneById(1L);
        //assert
        assertThat(foundProduct, equalTo(product));
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Test finding a product by id when product not found")
    void testFindProductByIdWhenProductNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.getOneById(1L));
        assertThat(ex.getMessage(), is("Product with id 1 not found"));
    }

    @Test
    @DisplayName("Test deleting a product by id")
    void testDeleteProductWhenOk() {
        //arrange
        ProductEntity product = factory.create();
        when(repository.findById(1L)).thenReturn(Optional.of(product));
        //act
        boolean result = service.deleteById(1L);
        //assert
        assertThat(result, is(true));
        verify(repository).delete(product);
    }

    @Test
    @DisplayName("Test deleting a product when delete throws an exception")
    void testDeleteProductWhenDeleteThrowsException() {
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
    @DisplayName("Test deleting a product by id when product not found")
    void testDeleteProductWhenProductNotFound() {
        //arrange
        when(repository.findById(1L)).thenReturn(Optional.empty());
        //act
        //assert
        var ex = assertThrows(MyEntityNotFoundException.class, () -> service.deleteById(1L));
        assertThat(ex.getMessage(), is("Product with id 1 not found"));

    }

    @Test
    @DisplayName("Test finding all products")
    void testFindAllProductsWhenOk() {
        //arrange
        List<ProductEntity> products = factory.createList(50);
        when(repository.findAll()).thenReturn(products);
        //act
        java.util.List<ProductEntity> foundProducts = service.getAll();
        //assert
        assertThat(foundProducts, equalTo(products));
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Test getting a list of Addresses paginated and sorted")
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
    @DisplayName("Test finding all products when no products found")
    void testEmptyListWhenNoProductsFound() {
        //arrange
        when(repository.findAll()).thenReturn(List.of());
        //act
        java.util.List<ProductEntity> foundProducts = service.getAll();
        //assert
        assertThat(foundProducts, equalTo(List.of()));
    }

    @Test
    @DisplayName("Test adding stock")
    void testAddingStockWhenOk() {
        //arrange
        ProductEntity product = factory.create();
        long id = product.getId();
        product.setStock(10);
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.of(product));
        when(repository.save(any(ProductEntity.class))).thenReturn(product);
        //act
        ProductEntity modifiedProduct = service.increaseStock(id, 5);
        //assert
        assertThat(modifiedProduct.getStock(), is(15));
        verify(repository).findById(id);
        verify(repository).save(product);
    }

    @Test
    @DisplayName("Test removing stock")
    void testRemovingStockWhenOk() {
        //arrange
        ProductEntity product = factory.create();
        long id = product.getId();
        product.setStock(10);
        when(repository.findById(anyLong())).thenReturn(java.util.Optional.of(product));
        when(repository.save(any(ProductEntity.class))).thenReturn(product);
        //act
        ProductEntity modifiedProduct = service.decreaseStock(id, 5);
        //assert
        assertThat(modifiedProduct.getStock(), is(5));
        verify(repository).findById(id);
        verify(repository).save(product);
    }


}