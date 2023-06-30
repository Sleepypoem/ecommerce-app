package com.sleepypoem.commerceapp.config.keycloak;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepypoem.commerceapp.config.beans.AuthServerPropertyLoader;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.utils.factories.impl.UserFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyCloakFacadeImplTest {

    KeyCloakFacadeImpl keyCloakFacade;

    @Mock
    KeyCloakAuthorizer keyCloakAuthorizer;

    @Mock
    RequestHelper requestHelper;

    String adminRestPrefix = "adminRestPrefix";

    String USERS_ENDPOINT = "/users";

    String realmName = "realmName";

    String accessToken = "accessToken";

    HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void setUp() {
        AuthServerPropertyLoader authServerPropertyLoader = new AuthServerPropertyLoader();
        authServerPropertyLoader.setAdminPassword("admin");
        authServerPropertyLoader.setAdminUsername("admin");
        authServerPropertyLoader.setClientId("clientId");
        authServerPropertyLoader.setClientSecret("clientSecret");
        authServerPropertyLoader.setRealmName("realmName");
        authServerPropertyLoader.setAdminRestPrefix(adminRestPrefix);
        keyCloakFacade = new KeyCloakFacadeImpl(new ObjectMapper(), authServerPropertyLoader, keyCloakAuthorizer, requestHelper);
    }

    @Test
    @DisplayName("Test create an user in the auth server")
    void testCreateUserWhenOk() {
        //arrange
        UserDto userDto = new UserFactory().create();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response = new ResponseEntity<>(HttpStatus.OK);
        String url = adminRestPrefix + realmName + USERS_ENDPOINT + "/";

        when(keyCloakAuthorizer.getAccessToken()).thenReturn(accessToken);
        when(requestHelper.createHeaders(any(), anyString())).thenReturn(headers);
        when(requestHelper.createHttpEntity(any(), any())).thenReturn(new HttpEntity<>(headers));
        when(requestHelper.makeRequest(any(), any(), any())).thenReturn(response);

        //act
        UserDto result = keyCloakFacade.createUser(userDto);

        //assert
        assertEquals(userDto, result);
        verify(requestHelper).createHeaders(MediaType.APPLICATION_JSON, accessToken);
        verify(requestHelper).createHttpEntity(userDto.toJsonString(), headers);
        verify(requestHelper).makeRequest(url, HttpMethod.POST, new HttpEntity<>(headers));
    }

    @Test
    @DisplayName("Test update an user in the auth server")
    void testUpdateUserWhenOk() {
        //arrange
        var userDto = new UserFactory().create();
        var response = new ResponseEntity<String>(HttpStatus.OK);
        String url = adminRestPrefix + realmName + USERS_ENDPOINT + "/1";
        when(keyCloakAuthorizer.getAccessToken()).thenReturn(accessToken);
        when(requestHelper.createHeaders(any(), anyString())).thenReturn(headers);
        when(requestHelper.createHttpEntity(any(), any())).thenReturn(new HttpEntity<>(headers));
        when(requestHelper.makeRequest(any(), any(), any())).thenReturn(response);
        //act
        var user = keyCloakFacade.updateUser("1", userDto);
        //assert
        assertThat(user, equalTo(userDto));
        verify(requestHelper).createHeaders(MediaType.APPLICATION_JSON, accessToken);
        verify(requestHelper).createHttpEntity(userDto.toJsonString(), headers);
        verify(requestHelper).makeRequest(url, HttpMethod.PUT, new HttpEntity<>(headers));
    }

    @Test
    @DisplayName("Test get an user by id from the auth server")
    void testGetUserByIdWhenOk() {
        //arrange
        String userId = "123";
        String url = adminRestPrefix + realmName + USERS_ENDPOINT + "/" + userId;
        headers.setContentType(MediaType.APPLICATION_JSON);
        String responseBody = new UserFactory().create().toJsonString();
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(keyCloakAuthorizer.getAccessToken()).thenReturn(accessToken);
        when(requestHelper.createHeaders(eq(MediaType.APPLICATION_JSON), anyString()))
                .thenReturn(headers);
        when(requestHelper.createEmptyHttpEntity(headers))
                .thenReturn(new HttpEntity<>(headers));
        when(requestHelper.makeRequest(eq(adminRestPrefix + realmName + USERS_ENDPOINT + "/" + userId), eq(HttpMethod.GET), any(HttpEntity.class)))
                .thenReturn(responseEntity);

        //act
        UserDto result = keyCloakFacade.getUserById(userId);

        //assert
        verify(requestHelper).createHeaders(MediaType.APPLICATION_JSON, keyCloakAuthorizer.getAccessToken());
        verify(requestHelper).makeRequest(eq(url), eq(HttpMethod.GET), any(HttpEntity.class));
        UserDto expectedUserDto = UserDto.fromJsonString(responseBody);
        assertThat(result, is(expectedUserDto));
    }

    @Test
    @DisplayName("Test get an user by username from the auth server when user not found")
    void testGetUserByUsernameWhenUserNotFound() {
        //arrange
        var response = new ResponseEntity<>("[]", HttpStatus.OK);
        when(requestHelper.makeRequest(any(), any(), any())).thenReturn(response);
        //act
        //assert
        var ex = assertThrows(MyUserNotFoundException.class, () -> keyCloakFacade.getUserByUserName("username"));
        assertThat(ex.getMessage(), equalTo("User with username: username, not found"));
    }

    @Test
    @DisplayName("Test get an user by username from the auth server when cannot parse response")
    void testGetUserByUsernameWhenCannotParseResponse() {
        //arrange
        var response = new ResponseEntity<>("{}", HttpStatus.OK);
        when(requestHelper.createHeaders(any(), any())).thenReturn(headers);
        when(requestHelper.makeRequest(any(), any(), any())).thenReturn(response);
        //act
        //assert
        var ex = assertThrows(MyInternalException.class, () -> keyCloakFacade.getUserByUserName("username"));
        assertThat(ex.getMessage(), equalTo("Error while parsing user list from keycloak server"));
    }

    @Test
    @DisplayName("Test get an user by username from the auth server")
    void testGetUserByUserNameWhenOk() {
        //arrange
        var userDto = new UserFactory().create();
        String body = "[" + userDto.toJsonString() + "]";
        var response = new ResponseEntity<>(body, HttpStatus.OK);
        String url = adminRestPrefix + realmName + USERS_ENDPOINT + "?username=username&exact=true";
        when(keyCloakAuthorizer.getAccessToken()).thenReturn(accessToken);
        when(requestHelper.createHeaders(any(), anyString())).thenReturn(new HttpHeaders());
        when(requestHelper.createEmptyHttpEntity(any())).thenReturn(new HttpEntity<>(headers));
        when(requestHelper.makeRequest(anyString(), eq(HttpMethod.GET), any(HttpEntity.class))).thenReturn(response);
        //act
        var user = keyCloakFacade.getUserByUserName("username");
        //assert
        assertThat(user, equalTo(userDto));
        verify(requestHelper).createHeaders(MediaType.APPLICATION_JSON, accessToken);
        verify(requestHelper).createEmptyHttpEntity(headers);
        verify(requestHelper).makeRequest(url, HttpMethod.GET, new HttpEntity<>(headers));
    }

    @Test
    @DisplayName("Test delete an user from the auth server")
    void testDeleteUserWhenOk() {
        //arrange
        var response = new ResponseEntity<>("", HttpStatus.OK);
        String url = adminRestPrefix + realmName + USERS_ENDPOINT + "/1";
        when(keyCloakAuthorizer.getAccessToken()).thenReturn(accessToken);
        when(requestHelper.createHeaders(any(), anyString())).thenReturn(headers);
        when(requestHelper.createEmptyHttpEntity(any())).thenReturn(new HttpEntity<>(headers));
        when(requestHelper.makeRequest(any(), any(), any())).thenReturn(response);
        //act
        boolean result = keyCloakFacade.deleteUser("1");
        //assert
        assertThat(result, is(true));
        verify(requestHelper).createHeaders(MediaType.APPLICATION_JSON, accessToken);
        verify(requestHelper).createEmptyHttpEntity(headers);
        verify(requestHelper).makeRequest(url, HttpMethod.DELETE, new HttpEntity<>(headers));
    }

    @Test
    @DisplayName("Test delete an user from the auth server when there is an error")
    void testDeleteUserWhenError() {
        //arrange
        when(requestHelper.makeRequest(any(), any(), any())).thenThrow(new RuntimeException());
        //act
        boolean result = keyCloakFacade.deleteUser("1");
        //assert
        assertThat(result, is(false));
        verify(requestHelper).makeRequest(any(), any(), any());
    }

    @Test
    @DisplayName("Test get all users from the auth server that match the criteria")
    void testGetAllUsersByCriteriaWhenOk() {
        //arrange
        String criteriaName = "username";
        String criteriaValue = "john.doe";
        Integer firstResult = 0;
        Integer maxResults = 10;
        boolean exact = true;
        String url = adminRestPrefix + realmName + "/users?" + criteriaName + "=" + criteriaValue + "&first=" + firstResult + "&max=" + maxResults + "&exact=" + exact;
        UserDto userDto = new UserFactory().create();

        headers.setContentType(MediaType.APPLICATION_JSON);
        String responseBody = "[" + userDto.toJsonString() + "]";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(keyCloakAuthorizer.getAccessToken()).thenReturn(accessToken);
        when(requestHelper.createHeaders(eq(MediaType.APPLICATION_JSON), anyString()))
                .thenReturn(headers);
        when(requestHelper.createEmptyHttpEntity(headers))
                .thenReturn(new HttpEntity<>(headers));
        when(requestHelper.makeRequest(eq(url), eq(HttpMethod.GET), any(HttpEntity.class)))
                .thenReturn(responseEntity);

        //act
        List<UserDto> result = keyCloakFacade.getAllUsersByCriteria(criteriaName, criteriaValue, firstResult, maxResults, exact);

        //assert
        verify(requestHelper).createHeaders(MediaType.APPLICATION_JSON, accessToken);
        verify(requestHelper).createEmptyHttpEntity(headers);
        verify(requestHelper).makeRequest(eq(url), eq(HttpMethod.GET), any(HttpEntity.class));

        assertThat(result, hasSize(1));
        assertThat(result, is(notNullValue()));

        assertThat(result.get(0), is(equalTo(userDto)));
    }

    @Test
    @DisplayName("Test get all users from the auth server that match the criteria when criteria is null")
    void testGetUsersByCriteriaWhenCriteriaIsNull() {
        //arrange
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> keyCloakFacade.getAllUsersByCriteria(null, "value", 0, 10, true));
        assertThat(ex.getMessage(), is("Criteria name cannot be null"));
        verifyNoInteractions(requestHelper);
    }

    @Test
    @DisplayName("Test get all users from the auth server that match the criteria when criteria value is null")
    void testGetUsersByCriteriaWhenCriteriaValueIsNull() {
        //arrange
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> keyCloakFacade.getAllUsersByCriteria("name", null, 0, 10, true));
        assertThat(ex.getMessage(), is("Criteria value cannot be null"));
        verifyNoInteractions(requestHelper);
    }

    @Test
    @DisplayName("Test counting all users from the auth server that match the criteria")
    void testCountUsersByCriteriaWhenOk() {
        //arrange
        String criteriaName = "username";
        String criteriaValue = "john.doe";
        String url = adminRestPrefix + realmName + "/users/count?" + criteriaName + "=" + criteriaValue;

        headers.setContentType(MediaType.APPLICATION_JSON);
        String responseBody = "1";
        ResponseEntity<String> responseEntity = new ResponseEntity<>(responseBody, HttpStatus.OK);
        when(keyCloakAuthorizer.getAccessToken()).thenReturn(accessToken);
        when(requestHelper.createHeaders(eq(MediaType.APPLICATION_JSON), anyString()))
                .thenReturn(headers);
        when(requestHelper.createEmptyHttpEntity(headers))
                .thenReturn(new HttpEntity<>(headers));
        when(requestHelper.makeRequest(eq(url), eq(HttpMethod.GET), any(HttpEntity.class)))
                .thenReturn(responseEntity);

        //act
        Long result = keyCloakFacade.countUsersByCriteria(criteriaName, criteriaValue);

        //assert
        verify(requestHelper).createHeaders(MediaType.APPLICATION_JSON, accessToken);
        verify(requestHelper).createEmptyHttpEntity(headers);
        verify(requestHelper).makeRequest(eq(url), eq(HttpMethod.GET), any(HttpEntity.class));

        assertThat(result, is(1L));
    }

    @Test
    @DisplayName("Test counting all users from the auth server that match the criteria when criteria is null")
    void testCountUsersByCriteriaWhenCriteriaIsNull() {
        //arrange
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> keyCloakFacade.countUsersByCriteria(null, "value"));
        assertThat(ex.getMessage(), is("Criteria name cannot be null"));
        verifyNoInteractions(requestHelper);
    }

    @Test
    @DisplayName("Test counting all users from the auth server that match the criteria when criteria value is null")
    void testCountUsersByCriteriaWhenCriteriaValueIsNull() {
        //arrange
        //act
        //assert
        var ex = assertThrows(MyBadRequestException.class, () -> keyCloakFacade.countUsersByCriteria("name", null));
        assertThat(ex.getMessage(), is("Criteria value cannot be null"));
        verifyNoInteractions(requestHelper);
    }
}