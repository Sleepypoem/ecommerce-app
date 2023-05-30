package com.sleepypoem.commerceapp.utils;


import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BaseMapperTest {

    @Test
    void testTwoProductsAreEqualsIfTheyHaveTheSameId() {
        // given
        ProductEntity testEntity1 = new ProductEntity();
        testEntity1.setId(1L);
        ProductEntity testEntity2 = new ProductEntity();
        testEntity2.setId(1L);
        // when
        boolean result = testEntity1.equals(testEntity2);
        // then
        assertTrue(result);
    }

    @Test
    void testTwoProductsAreNotEqualsIfTheyHaveDifferentId() {
        // given
        ProductEntity testEntity1 = new ProductEntity();
        testEntity1.setId(1L);
        ProductEntity testEntity2 = new ProductEntity();
        testEntity2.setId(2L);
        // when
        boolean result = testEntity1.equals(testEntity2);
        // then
        assertFalse(result);
    }

    @Test
    void testTwoEntitiesAreNotEqualsIfTheyAreNotTheSame() {
        // given
        ProductEntity testEntity1 = new ProductEntity();
        testEntity1.setId(1L);
        AddressEntity testEntity2 = new AddressEntity();
        testEntity2.setId(1L);
        // when
        boolean result = testEntity1.equals(testEntity2);
        // then
        assertFalse(result);
    }
}
