package com.sleepypoem.commerceapp.config.keycloak;

import com.sleepypoem.commerceapp.config.beans.AuthServerPropertyLoader;
import com.sleepypoem.commerceapp.domain.dto.AuthServerResponseDto;
import com.sleepypoem.commerceapp.utils.factories.impl.AuthServerResponseFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KeyCloakAuthorizerImplTest {

    KeyCloakAuthorizerImpl keycloakAuthorizer;

    @Mock
    RequestHelper requestHelper;

    @BeforeEach
    void setUp() {
        AuthServerPropertyLoader authServerPropertyLoader = new AuthServerPropertyLoader();
        authServerPropertyLoader.setAdminPassword("admin");
        authServerPropertyLoader.setAdminUsername("admin");
        authServerPropertyLoader.setClientId("clientId");
        keycloakAuthorizer = new KeyCloakAuthorizerImpl(authServerPropertyLoader, requestHelper);
    }

    @Test
    @DisplayName("Test get access token when token is null")
    void testGetAccessTokenWhenOk() {
        //arrange
        AuthServerResponseDto authServerResponseDto = new AuthServerResponseFactory().create();
        var response = new ResponseEntity<>(authServerResponseDto.toJsonString(), HttpStatusCode.valueOf(200));
        when(requestHelper.createHeaders(any(), eq(null))).thenReturn(new HttpHeaders());
        when(requestHelper.makeRequest(any(), eq(HttpMethod.POST), any())).thenReturn(response);
        //act
        var token = keycloakAuthorizer.getAccessToken();
        //assert
        assertThat(token, is(notNullValue()));
    }

    @Test
    @DisplayName("Test refresh token when token has expired")
    void testRefreshTokenWhenOk() {
        //arrange
        AuthServerResponseDto authServerResponseDto = new AuthServerResponseFactory().create();
        AuthServerResponseDto expired = new AuthServerResponseFactory().createWithExpiredToken();
        var response = new ResponseEntity<>(authServerResponseDto.toJsonString(), HttpStatusCode.valueOf(200));
        when(requestHelper.createHeaders(any(), eq(null))).thenReturn(new HttpHeaders());
        when(requestHelper.makeRequest(any(), eq(HttpMethod.POST), any())).thenReturn(response);
        //act
        keycloakAuthorizer.setServerResponse(expired);
        var result = keycloakAuthorizer.getAccessToken();
        //assert
        assertThat(result, is(notNullValue()));
    }
}