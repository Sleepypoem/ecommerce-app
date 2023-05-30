package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    @DisplayName("Test creating a product")
    void testCreateProduct() {
        //arrange
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setStock(10);
        product.setPrice(100d);
        when(productRepository.save(product)).thenReturn(product);
        //act
        ProductEntity createdProduct = productService.create(product);
        //assert
        assertEquals(product, createdProduct);
    }

    @Test
    @DisplayName("Test updating a product")
    void testUpdateProduct() {
        //arrange
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setStock(10);
        product.setPrice(100d);
        when(productRepository.save(product)).thenReturn(product);
        //act
        ProductEntity updatedProduct = productService.update(1L, product);
        //assert
        assertEquals(product, updatedProduct);
    }

    @Test
    @DisplayName("Test finding a product by id")
    void testFindProductById() {
        //arrange
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setStock(10);
        product.setPrice(100d);
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));
        //act
        ProductEntity foundProduct = productService.getOneById(1L);
        //assert
        assertEquals(product, foundProduct);
    }

    @Test
    @DisplayName("Test finding a product by id when product not found")
    void testFindProductByIdWhenProductNotFound() {
        //arrange
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> productService.getOneById(1L));
    }

    @Test
    @DisplayName("Test deleting a product by id")
    void testDeleteProduct() {
        //arrange
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setStock(10);
        product.setPrice(100d);
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));
        //act
        productService.delete(1L);
        //assert
        verify(productRepository).delete(product);
    }

    @Test
    @DisplayName("Test deleting a product by id when product not found")
    void testDeleteProductWhenProductNotFound() {
        //arrange
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> productService.delete(1L));
    }

    @Test
    @DisplayName("Test finding all products")
    void testFindAllProducts() {
        //arrange
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setStock(10);
        product.setPrice(100d);
        when(productRepository.findAll()).thenReturn(List.of(product));
        //act
        java.util.List<ProductEntity> foundProducts = productService.getAll();
        //assert
        assertEquals(List.of(product), foundProducts);
    }

    @Test
    @DisplayName("Test finding all products when no products found")
    void testEmptyListWhenNoProductsFound() {
        //arrange
        when(productRepository.findAll()).thenReturn(java.util.Collections.emptyList());
        //act
        java.util.List<ProductEntity> foundProducts = productService.getAll();
        //assert
        assertEquals(java.util.Collections.emptyList(), foundProducts);
    }

    @Test
    @DisplayName("Test stock modification")
    void testStockModification() {
        //arrange
        ProductEntity product = new ProductEntity();
        product.setId(1L);
        product.setStock(10);
        product.setPrice(100d);
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        //act
        ProductEntity modifiedProduct = productService.modifyStock(1L, 5);
        //assert
        assertEquals(5, modifiedProduct.getStock());
    }

    @Test
    @DisplayName("Test stock modification when product not found")
    void testStockModificationWhenProductNotFound() {
        //arrange
        when(productRepository.findById(1L)).thenReturn(java.util.Optional.empty());
        //act
        //assert
        assertThrows(MyEntityNotFoundException.class, () -> productService.modifyStock(1L, 5));
    }

}