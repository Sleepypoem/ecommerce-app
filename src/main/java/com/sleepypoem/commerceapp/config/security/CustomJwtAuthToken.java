package com.sleepypoem.commerceapp.config.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.util.Collection;
import java.util.Map;

public class CustomJwtAuthToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {

    protected CustomJwtAuthToken(Jwt token, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(token, principal, credentials, authorities);
        this.setAuthenticated(true);
    }

    @Override
    public Map<String, Object> getTokenAttributes() {
        return this.getToken().getClaims();
    }
}
