package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.exceptions.MyEntityNotFoundException;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.services.ProductService;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.utils.factories.impl.CheckoutFactory;
import com.sleepypoem.commerceapp.utils.factories.impl.ProductFactory;
import com.sleepypoem.commerceapp.utils.factories.impl.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateCheckoutTest {

    ValidateCheckout validateCheckout;

    @Mock
    UserService userService;

    @Mock
    ProductService productService;

    CheckoutFactory checkoutFactory;

    @BeforeEach
    void setUp() {
        validateCheckout = new ValidateCheckout(userService, productService);
        checkoutFactory = new CheckoutFactory();
    }

    @Test
    @DisplayName("Test validateCheckout when ok")
    void testValidateCheckoutWhenOk() {
        //arrange
        var checkout = checkoutFactory.create();
        when(productService.getOneById(anyLong())).thenReturn(new ProductFactory().create());
        when(userService.getOneById(anyString())).thenReturn(new UserFactory().create());
        //act
        Map<String, String> result = validateCheckout.isValid(checkout);
        //assert
        assertThat(result, is(Map.of()));
    }

    @Test
    @DisplayName("Test validateCheckout when user not found")
    void testValidateCheckoutWhenUserNotFound() {
        //arrange
        var checkout = checkoutFactory.create();
        when(productService.getOneById(anyLong())).thenReturn(new ProductFactory().create());
        when(userService.getOneById(anyString())).thenThrow(new MyUserNotFoundException("User not found"));
        //act
        Map<String, String> result = validateCheckout.isValid(checkout);
        //assert
        assertThat(result, is(Map.of("userId", "User with id: " + checkout.getUserId() + " not found.")));
    }

    @Test
    @DisplayName("Test validateCheckout when product not found")
    void testValidateCheckoutWhenProductNotFound() {
        //arrange
        var checkout = checkoutFactory.create();
        when(productService.getOneById(anyLong())).thenThrow(new MyEntityNotFoundException("Product not found"));
        when(userService.getOneById(anyString())).thenReturn(new UserFactory().create());
        //act
        Map<String, String> result = validateCheckout.isValid(checkout);
        //assert
        assertThat(result, is(Map.of("product", "Product with id: " + checkout.getItems().get(0).getProduct().getId() + " not found.")));
    }

    @Test
    @DisplayName("Test cannot create checkout with COMPLETED Status")
    void testValidateCheckoutWhenCheckoutIsCompleted() {
        //arrange
        var checkout = checkoutFactory.create();
        checkout.setStatus(CheckoutStatus.COMPLETED);
        checkout.setId(null);
        when(productService.getOneById(anyLong())).thenReturn(new ProductFactory().create());
        when(userService.getOneById(anyString())).thenReturn(new UserFactory().create());
        //act
        Map<String, String> result = validateCheckout.isValid(checkout);
        //assert
        assertThat(result, is(Map.of("status", "Must provide a pending checkout. Provided status: " + checkout.getStatus() + ".")));
    }

    @Test
    @DisplayName("Test cannot create checkout with CANCELLED Status")
    void testValidateCheckoutWhenCheckoutIsCancelled() {
        //arrange
        var checkout = checkoutFactory.create();
        checkout.setStatus(CheckoutStatus.CANCELLED);
        checkout.setId(null);
        when(productService.getOneById(anyLong())).thenReturn(new ProductFactory().create());
        when(userService.getOneById(anyString())).thenReturn(new UserFactory().create());
        //act
        Map<String, String> result = validateCheckout.isValid(checkout);
        //assert
        assertThat(result, is(Map.of("status", "Must provide a pending checkout. Provided status: " + checkout.getStatus() + ".")));
    }

    @Test
    @DisplayName("Test cannot create checkout with an item with quantity less than 1")
    void testValidateCheckoutWhenCheckoutHasItemWithQuantityLessThanOne() {
        //arrange
        var checkout = checkoutFactory.create();
        checkout.setStatus(CheckoutStatus.PENDING);
        checkout.setId(null);
        checkout.getItems().get(0).setQuantity(0);
        when(productService.getOneById(anyLong())).thenReturn(new ProductFactory().create());
        when(userService.getOneById(anyString())).thenReturn(new UserFactory().create());
        //act
        Map<String, String> result = validateCheckout.isValid(checkout);
        //assert
        assertThat(result, is(Map.of("quantity", "The quantity must be greater than 0.")));
    }

    @Test
    @DisplayName("Test cannot create checkout when quantity is greater than stock")
    void testValidateCheckoutWhenCheckoutHasItemWithQuantityGreaterThanStock() {
        //arrange
        var checkout = checkoutFactory.create();
        var product = new ProductFactory().create();
        product.setStock(10);
        checkout.getItems().get(0).setQuantity(100);
        when(productService.getOneById(anyLong())).thenReturn(product);
        when(userService.getOneById(anyString())).thenReturn(new UserFactory().create());
        //act
        Map<String, String> result = validateCheckout.isValid(checkout);
        //assert
        assertThat(result, is(Map.of("stock", "The stock is not enough. Current stock: 10.")));
    }

}