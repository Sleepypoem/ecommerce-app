package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.utils.factories.impl.CheckoutItemFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidateCheckoutItemTest {

    ValidateCheckoutItem validateCheckoutItem;

    CheckoutItemFactory checkoutItemFactory;

    @BeforeEach
    void setUp() {
        validateCheckoutItem = new ValidateCheckoutItem();
        checkoutItemFactory = new CheckoutItemFactory();
    }

    @Test
    @DisplayName("Test validateCheckoutItem when no errors")
    void testValidateCheckoutItemWhenNoErrors() {
        //arrange
        var checkoutItem = checkoutItemFactory.create();
        //act
        var result = validateCheckoutItem.isValid(checkoutItem);
        //assert
        assertEquals(result, Map.of());
    }

    @Test
    @DisplayName("Test validateCheckoutItem when quantity is less than 0")
    void testValidateCheckoutItemWhenQuantityIsLessThan0() {
        //arrange
        var checkoutItem = checkoutItemFactory.create();
        checkoutItem.setQuantity(-1);
        //act
        var result = validateCheckoutItem.isValid(checkoutItem);
        //assert
        assertEquals(result, Map.of("quantity", "The quantity cannot be less than 0."));
    }

}