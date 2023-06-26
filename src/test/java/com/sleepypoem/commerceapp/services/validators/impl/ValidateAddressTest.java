package com.sleepypoem.commerceapp.services.validators.impl;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.services.UserService;
import com.sleepypoem.commerceapp.utils.factories.impl.AddressFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateAddressTest {

    ValidateAddress validateAddress;

    AddressFactory addressFactory;

    @Mock
    UserService userService;

    @BeforeEach
    void setUp() {
        validateAddress = new ValidateAddress(userService);
        addressFactory = new AddressFactory();
    }

    @Test
    @DisplayName("Test validate when no errors")
    void testValidateAddressWhenOk() {
        //arrange
        AddressEntity addressEntity = addressFactory.create();
        when(userService.getOneById(anyString())).thenReturn(new UserDto());
        //act
        Map<String, String> result = validateAddress.isValid(addressEntity);
        //assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test validate address when user not found")
    void testValidateAddressWhenUserNotFound() {
        //arrange
        AddressEntity addressEntity = addressFactory.create();
        doThrow(new MyUserNotFoundException("User not found")).when(userService).getOneById(anyString());
        //act
        Map<String, String> result = validateAddress.isValid(addressEntity);
        //assert
        assertThat(result, hasEntry("userId", "User with id: " + addressEntity.getUserId() + " not found."));
    }

}