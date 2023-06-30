package com.sleepypoem.commerceapp.config.keycloak;

public interface KeyCloakAuthorizer {
    String getAccessToken();

    void refreshToken();
}
