package com.sleepypoem.commerceapp.utils;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import org.junit.jupiter.api.Test;

public class TestEntity {

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
        assert result;
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
        assert !result;
    }
}
