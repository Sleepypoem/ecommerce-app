package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.utils.factories.impl.ProductFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ValidateProductTest {

    ValidateProduct validateProduct;

    ProductFactory productFactory;

    @BeforeEach
    void setUp() {
        validateProduct = new ValidateProduct();
        productFactory = new ProductFactory();
    }

    @Test
    @DisplayName("Test validate product when no errors")
    void isValid() {
        //arrange
        var product = productFactory.create();
        //act
        var result = validateProduct.isValid(product);
        //assert
        assertThat(result, is(Map.of()));
    }

    @Test
    @DisplayName("Test validate product when stock is less than 0")
    void isValidWhenStockIsLessThan0() {
        //arrange
        var product = productFactory.create();
        product.setStock(-1);
        //act
        var result = validateProduct.isValid(product);
        //assert
        assertThat(result, is(Map.of("stock", "The stock cannot be less than 0.")));
    }

    @Test
    @DisplayName("Test validate product when price is less than 0")
    void isValidWhenPriceIsLessThan0() {
        //arrange
        var product = productFactory.create();
        product.setPrice(-1.0);
        //act
        var result = validateProduct.isValid(product);
        //assert
        assertThat(result, is(Map.of("price", "The price cannot be less than 0.")));
    }

}