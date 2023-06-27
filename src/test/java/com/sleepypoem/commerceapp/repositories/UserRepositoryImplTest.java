package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.config.keycloak.KeyCloakFacade;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
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
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static com.sleepypoem.commerceapp.utils.TestConstants.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    UserRepositoryImpl userRepositoryImpl;

    @Mock
    KeyCloakFacade keyCloakFacade;

    UserFactory factory;

    @BeforeEach
    void setUp() {
        userRepositoryImpl = new UserRepositoryImpl(keyCloakFacade);
        factory = new UserFactory();
    }

    @Test
    @DisplayName("Test create user")
    void testCreateUserWhenOk() {
        //arrange
        UserDto userDto = factory.create();
        when(keyCloakFacade.createUser(any(UserDto.class))).thenReturn(userDto);
        // act
        UserDto result = userRepositoryImpl.create(userDto);
        // assert
        assertThat(result, is(equalTo(userDto)));
        verify(keyCloakFacade).createUser(userDto);
    }

    @Test
    @DisplayName("Test create user when userDto is null")
    void testCreateUserWhenUserDtoIsNull() {
        //arrange
        // act
        // assert
        var ex = assertThrows(MyBadRequestException.class, () -> userRepositoryImpl.create(null));
        assertThat(ex.getMessage(), is("UserDto passed to create method is null"));
        verify(keyCloakFacade, never()).createUser(any(UserDto.class));
    }

    @Test
    @DisplayName("Test update user")
    void testUpdateUserWhenOk() {
        //arrange
        UserDto userDto = factory.create();
        when(keyCloakFacade.updateUser(anyString(), any(UserDto.class))).thenReturn(userDto);
        // act
        UserDto result = userRepositoryImpl.update(userDto.getId(), userDto);
        // assert
        assertThat(result, is(equalTo(userDto)));
        verify(keyCloakFacade).updateUser(userDto.getId(), userDto);
    }

    @Test
    @DisplayName("Test update user when userDto is null")
    void testUpdateUserWhenUserDtoIsNull() {
        //arrange
        String id = "id";
        // act
        // assert
        var ex = assertThrows(MyBadRequestException.class, () -> userRepositoryImpl.update(id, null));
        assertThat(ex.getMessage(), is("UserDto passed to update method is null"));
        verify(keyCloakFacade, never()).updateUser(anyString(), any(UserDto.class));
    }

    @Test
    @DisplayName("Test update user when id passed in method does not match id in userDto")
    void testUpdateUserWhenIdDoesNotMatch() {
        //arrange
        UserDto userDto = factory.create();
        String incorrectId = "incorrectId";
        // act
        // assert
        var ex = assertThrows(MyBadRequestException.class, () -> userRepositoryImpl.update(incorrectId, userDto));
        assertThat(ex.getMessage(), is("User id passed to update method is not equal to user id in UserDto"));
        verifyNoInteractions(keyCloakFacade);
    }

    @Test
    @DisplayName("Test get user by id")
    void testGetUserByIdWhenOk() {
        //arrange
        UserDto userDto = factory.create();
        when(keyCloakFacade.getUserById(anyString())).thenReturn(userDto);
        // act
        UserDto result = userRepositoryImpl.getUserById(userDto.getId());
        // assert
        assertThat(result, is(equalTo(userDto)));
        verify(keyCloakFacade).getUserById(userDto.getId());
    }

    @Test
    @DisplayName("Test get user by username")
    void testGetUserByUserNameWhenOk() {
        //arrange
        UserDto userDto = factory.create();
        when(keyCloakFacade.getUserByUserName(anyString())).thenReturn(userDto);
        // act
        UserDto result = userRepositoryImpl.getUserByUserName(userDto.getUsername());
        // assert
        assertThat(result, is(equalTo(userDto)));
        verify(keyCloakFacade).getUserByUserName(userDto.getUsername());
    }

    @Test
    @DisplayName("Test delete user")
    void testDeleteUserWhenOk() {
        //arrange
        UserDto userDto = factory.create();
        when(keyCloakFacade.deleteUser(anyString())).thenReturn(true);
        // act
        boolean result = userRepositoryImpl.deleteUser(userDto.getId());
        // assert
        assertThat(result, is(true));
        verify(keyCloakFacade).deleteUser(userDto.getId());
    }

    @Test
    @DisplayName("Test get all users with a given criteria")
    void testGetAllUsersByCriteriaWhenOk() {
        //arrange
        String criteriaName = "username";
        String criteriaValue = "test";
        int firstResult = 0;
        int maxResults = 10;

        List<UserDto> userDtoList = factory.createList(50);
        when(keyCloakFacade.getAllUsersByCriteria(anyString(), anyString(), anyInt(), anyInt(), eq(true))).thenReturn(userDtoList);
        // act
        List<UserDto> result = userRepositoryImpl.getAllUsersByCriteria(criteriaName, criteriaValue, firstResult, maxResults, true);
        // assert
        assertThat(result, is(equalTo(userDtoList)));
        verify(keyCloakFacade).getAllUsersByCriteria(criteriaName, criteriaValue, firstResult, maxResults, true);
    }

    @Test
    @DisplayName("Test get all users with a given criteria when no users found")
    void testGetAllUsersByCriteriaWhenNoUsersFound() {
        //arrange
        String criteriaName = "username";
        String criteriaValue = "test";
        int firstResult = 0;
        int maxResults = 10;

        when(keyCloakFacade.getAllUsersByCriteria(anyString(), anyString(), anyInt(), anyInt(), eq(true))).thenReturn(List.of());
        // act
        List<UserDto> result = userRepositoryImpl.getAllUsersByCriteria(criteriaName, criteriaValue, firstResult, maxResults, true);
        // assert
        assertThat(result, is(empty()));
        verify(keyCloakFacade).getAllUsersByCriteria(criteriaName, criteriaValue, firstResult, maxResults, true);
    }

    @Test
    @DisplayName("Test get all users with a given criteria paginated and sorted")
    void testGetAllUserByCriteriaPaginatedAndSortedWhenOk() {
        //arrange
        String criteriaName = "username";
        String criteriaValue = "test";
        String nextPage = "/api/users?criteriaName=" + criteriaName + "&criteriaValue=" + criteriaValue + "&page=" + (DEFAULT_PAGE + 1) + "&size=" + DEFAULT_SIZE;

        List<UserDto> userDtoList = factory.createList(Math.toIntExact(DEFAULT_TOTAL_ELEMENTS));
        when(keyCloakFacade.countUsersByCriteria(anyString(), anyString())).thenReturn(DEFAULT_TOTAL_ELEMENTS);
        when(keyCloakFacade.getAllUsersByCriteria(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenReturn(userDtoList);
        // act
        PaginatedDto<UserDto> result = userRepositoryImpl.getAllUsersByCriteriaPaginatedAndSorted(criteriaName, criteriaValue, DEFAULT_PAGE, DEFAULT_SIZE, true);
        // assert
        assertAll(
                () -> assertThat(result.getTotalElements(), is(DEFAULT_TOTAL_ELEMENTS)),
                () -> assertThat(result.getCurrentPage(), is(DEFAULT_PAGE)),
                () -> assertThat(result.getContent(), is(equalTo(userDtoList))),
                () -> assertThat(result.getNextPage(), is(equalTo(nextPage))),
                () -> assertThat(result.getPreviousPage(), is(nullValue()))
        );
        verify(keyCloakFacade).getAllUsersByCriteria(criteriaName, criteriaValue, DEFAULT_PAGE, DEFAULT_SIZE, true);
    }

    @Test
    @DisplayName("Test get all users with a given criteria paginated and sorted when no users found")
    void testGetAllUsersByCriteriaPaginatedAndSortedWhenNoUsersFound() {
        //arrange
        String criteriaName = "username";
        String criteriaValue = "test";

        when(keyCloakFacade.countUsersByCriteria(anyString(), anyString())).thenReturn(0L);
        when(keyCloakFacade.getAllUsersByCriteria(anyString(), anyString(), anyInt(), anyInt(), anyBoolean())).thenReturn(List.of());
        // act
        PaginatedDto<UserDto> result = userRepositoryImpl.getAllUsersByCriteriaPaginatedAndSorted(criteriaName, criteriaValue, DEFAULT_PAGE, DEFAULT_SIZE, true);
        // assert
        assertAll(
                () -> assertThat(result.getTotalElements(), is(ZERO_TOTAL_ELEMENTS)),
                () -> assertThat(result.getCurrentPage(), is(DEFAULT_PAGE)),
                () -> assertThat(result.getContent(), is(empty())),
                () -> assertThat(result.getNextPage(), is(nullValue())),
                () -> assertThat(result.getPreviousPage(), is(nullValue()))
        );
        verify(keyCloakFacade).getAllUsersByCriteria(criteriaName, criteriaValue, DEFAULT_PAGE, DEFAULT_SIZE, true);
    }
}