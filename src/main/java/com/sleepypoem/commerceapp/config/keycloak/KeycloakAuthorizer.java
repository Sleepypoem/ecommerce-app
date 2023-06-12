package com.sleepypoem.commerceapp.config.keycloak;

public interface KeycloakAuthorizer {
    String getAccessToken();

    void refreshToken();
}
