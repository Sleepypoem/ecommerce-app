package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.repositories.UserRepository;
import com.sleepypoem.commerceapp.utils.factories.impl.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    UserService userService;

    @Mock
    UserRepository userRepository;

    UserFactory userFactory;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        userFactory = new UserFactory();
    }

    @Test
    @DisplayName("Test create user")
    void testCreateUserWhenOk() {
        //arrange
        UserDto userDto = userFactory.create();
        when(userRepository.create(any(UserDto.class))).thenReturn(userDto);
        //act
        String result = userService.create(userDto);
        //assert
        assertEquals(userDto.getId(), result);
        verify(userRepository).create(userDto);
    }

    @Test
    @DisplayName("Test delete user")
    void testDeleteUserWhenOk() {
        //arrange
        String id = "id";
        when(userRepository.deleteUser(anyString())).thenReturn(true);
        //act
        boolean result = userService.delete(id);
        //assert
        assertThat(result, is(true));
        verify(userRepository).deleteUser(id);
    }

    @Test
    @DisplayName("Test update user")
    void testUpdateUserWhenOk() {
        //arrange
        UserDto userDto = userFactory.create();
        when(userRepository.update(anyString(), any(UserDto.class))).thenReturn(userDto);
        //act
        UserDto result = userService.update(userDto.getId(), userDto);
        //assert
        assertEquals(userDto, result);
        verify(userRepository).update(userDto.getId(), userDto);
    }

    @Test
    @DisplayName("Test get user by username")
    void testGetUserByUserNameWhenOk() {
        //arrange
        UserDto userDto = userFactory.create();
        when(userRepository.getUserByUserName(anyString())).thenReturn(userDto);
        //act
        UserDto result = userService.getOneByUserName(userDto.getUsername());
        //assert
        assertEquals(userDto, result);
        verify(userRepository).getUserByUserName(userDto.getUsername());
    }

    @Test
    @DisplayName("Test get user by id")
    void testGetUserByIdWhenOk() {
        //arrange
        UserDto userDto = userFactory.create();
        when(userRepository.getUserById(anyString())).thenReturn(userDto);
        //act
        UserDto result = userService.getOneById(userDto.getId());
        //assert
        assertEquals(userDto, result);
        verify(userRepository).getUserById(userDto.getId());
    }

    @Test
    @DisplayName("Test get all users by criteria")
    void testGetAllUsersByCriteriaWhenOk() {
        //arrange
        List<UserDto> userDtos = userFactory.createList(50);
        when(userRepository.getAllUsersByCriteria(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenReturn(userDtos);
        //act
        List<UserDto> result = userService.getAllByCriteria("username", "username", 0, 10, true);
        //assert
        assertEquals(userDtos, result);
        verify(userRepository).getAllUsersByCriteria("username", "username", 0, 10, true);
    }

}