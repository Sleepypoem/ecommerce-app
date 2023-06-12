package com.sleepypoem.commerceapp.config.keycloak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepypoem.commerceapp.config.beans.AuthServerPropertyLoader;
import com.sleepypoem.commerceapp.domain.dto.AuthServerResponseDto;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
public class KeycloakAuthorizerImpl implements KeycloakAuthorizer {

    private static final int REFRESH_TOKEN = 0;

    private static final int ACCESS_TOKEN = 1;

    private final String clientSecret;

    private final String tokenEndpoint;

    private final String authServerAdminUserName;

    private final String authServerAdminPassword;

    private final String authServerClientId;

    private AuthServerResponseDto serverResponse;

    private final ObjectMapper mapper;

    public KeycloakAuthorizerImpl(ObjectMapper mapper, AuthServerPropertyLoader authServerPropertyLoader) {
        this.clientSecret = authServerPropertyLoader.getClientSecret();
        this.tokenEndpoint = authServerPropertyLoader.getTokenEndpoint();
        this.authServerAdminUserName = authServerPropertyLoader.getAdminUsername();
        this.authServerAdminPassword = authServerPropertyLoader.getAdminPassword();
        this.authServerClientId = authServerPropertyLoader.getClientId();
        this.mapper = mapper;
    }

    @Override
    public String getAccessToken() {
        if (serverResponse == null || isTokenExpired(Long.valueOf(serverResponse.getRefreshExpiresIn()))) {
            log.info("Token not found or refresh token expired. Obtaining new token...");
            obtainAccessToken();
        } else if (isTokenExpired(Long.valueOf(serverResponse.getIssuedAt() + serverResponse.getExpiresIn()))) {
            log.info("Token expired. Refreshing...");
            refreshToken();
        }
        return serverResponse.getAccessToken();
    }

    @Override
    public void refreshToken() {
        this.serverResponse = connectWithAuthServer(REFRESH_TOKEN);
    }

    public void obtainAccessToken() {
        this.serverResponse = connectWithAuthServer(ACCESS_TOKEN);
    }

    private MultiValueMap<String, String> createRequestBodyForRefreshToken() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", authServerClientId);
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("client_secret", clientSecret);
        requestBody.add("refresh_token", serverResponse.getRefreshToken());
        return requestBody;
    }

    private MultiValueMap<String, String> createRequestBodyForAccessToken() {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("client_id", authServerClientId);
        requestBody.add("username", authServerAdminUserName);
        requestBody.add("password", authServerAdminPassword);
        requestBody.add("grant_type", "password");
        requestBody.add("client_secret", clientSecret);
        return requestBody;
    }

    public AuthServerResponseDto connectWithAuthServer(int action) {
        log.info("Connecting with auth server...");
        HttpHeaders headers = RequestCommons.createHeaders(MediaType.APPLICATION_FORM_URLENCODED, null);
        MultiValueMap<String, String> requestBody;
        if (action == 0) {
            requestBody = createRequestBodyForRefreshToken();
        } else {
            requestBody = createRequestBodyForAccessToken();
        }

        HttpEntity<MultiValueMap<String, String>> request = RequestCommons.createHttpEntity(requestBody, headers);
        ResponseEntity<String> response = RequestCommons.makeRequest(tokenEndpoint, HttpMethod.POST, request);
        AuthServerResponseDto responseDto = mapResponseToDto(response);

        responseDto.setIssuedAt(System.currentTimeMillis());

        return responseDto;
    }

    private AuthServerResponseDto mapResponseToDto(ResponseEntity<String> response) {
        AuthServerResponseDto responseDto;
        try {
            responseDto = mapper.readValue(response.getBody(), AuthServerResponseDto.class);
        } catch (JsonProcessingException e) {
            log.error("Error while parsing token response: " + e.getMessage());
            throw new MyInternalException("Error while parsing token response", e);
        }
        return responseDto;
    }

    private boolean isTokenExpired(Long expirationTime) {
        return System.currentTimeMillis() > expirationTime;
    }

}
