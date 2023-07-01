package com.sleepypoem.commerceapp.services.validators;

import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DetailedValidatorTest {

    DetailedValidator<ProductEntity> detailedValidator;

    @Mock
    IValidator<ProductEntity> iValidator;

    ProductEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new ProductEntity();
        detailedValidator = new DetailedValidator<>(iValidator, testEntity);
    }

    @Test
    @DisplayName("Test validation when everything is ok")
    void testValidationWhenEverythingIsOk() {
        //arrange
        when(iValidator.isValid(testEntity)).thenReturn(Map.of());
        //act
        detailedValidator.validate();
        //assert
        verify(iValidator).isValid(testEntity);
    }

    @Test
    @DisplayName("Test validation when something is wrong")
    void testValidationWhenSomethingIsWrong() {
        //arrange
        String message = """
                The following errors were found during validation : {
                Field: testField || Error: testError
                }""";
        when(iValidator.isValid(testEntity)).thenReturn(Map.of("testField", "testError"));
        //act
        Exception ex = assertThrows(MyValidationException.class, () -> detailedValidator.validate());
        //assert
        assertEquals(message, ex.getMessage());
        verify(iValidator).isValid(testEntity);
    }

}