package com.sleepypoem.commerceapp.config.keycloak;

import com.sleepypoem.commerceapp.config.beans.AuthServerPropertyLoader;
import com.sleepypoem.commerceapp.domain.dto.AuthServerResponseDto;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Slf4j
@Component
@Data
public class KeyCloakAuthorizerImpl implements KeyCloakAuthorizer {

    private static final int REFRESH_TOKEN = 0;

    private static final int ACCESS_TOKEN = 1;

    private final String clientSecret;

    private final String tokenEndpoint;

    private final String authServerAdminUserName;

    private final String authServerAdminPassword;

    private final String authServerClientId;

    private AuthServerResponseDto serverResponse;

    private final RequestHelper requestHelper;

    public KeyCloakAuthorizerImpl(AuthServerPropertyLoader authServerPropertyLoader, RequestHelper requestHelper) {
        this.clientSecret = authServerPropertyLoader.getClientSecret();
        this.tokenEndpoint = authServerPropertyLoader.getTokenEndpoint();
        this.authServerAdminUserName = authServerPropertyLoader.getAdminUsername();
        this.authServerAdminPassword = authServerPropertyLoader.getAdminPassword();
        this.authServerClientId = authServerPropertyLoader.getClientId();
        this.requestHelper = requestHelper;
    }

    @Override
    public String getAccessToken() {
        if (serverResponse == null) {
            obtainAccessToken();
        } else if (tokenNeedsRefresh()) {
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
        HttpHeaders headers = requestHelper.createHeaders(MediaType.APPLICATION_FORM_URLENCODED, null);
        MultiValueMap<String, String> requestBody;
        if (action == 0) {
            requestBody = createRequestBodyForRefreshToken();
        } else {
            requestBody = createRequestBodyForAccessToken();
        }

        HttpEntity<MultiValueMap<String, String>> request = requestHelper.createHttpEntity(requestBody, headers);
        ResponseEntity<String> response = requestHelper.makeRequest(tokenEndpoint, HttpMethod.POST, request);
        AuthServerResponseDto responseDto = AuthServerResponseDto.fromJsonString(response.getBody());

        responseDto.setIssuedAt(System.currentTimeMillis());

        return responseDto;
    }

    private boolean tokenNeedsRefresh() {
        boolean isRefreshTokenExpired = isTokenExpired(serverResponse.getIssuedAt() + Long.parseLong(serverResponse.getRefreshExpiresIn()));
        boolean isAccessTokenExpired = isTokenExpired(serverResponse.getIssuedAt() + Long.parseLong(serverResponse.getExpiresIn()));
        return isRefreshTokenExpired || isAccessTokenExpired;
    }

    private boolean isTokenExpired(Long expirationTime) {
        return System.currentTimeMillis() > expirationTime;
    }

}
