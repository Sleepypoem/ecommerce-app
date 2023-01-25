package com.sleepypoem.commerceapp.config.security;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.Transient;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;

import java.io.Serial;
import java.util.Collection;
import java.util.Map;

@Transient
public class UserToken extends AbstractOAuth2TokenAuthenticationToken<Jwt> {
    @Serial
    private static final long serialVersionUID = 600L;

    private String name;

    protected UserToken(Jwt token, Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String name) {
        super(token, principal, credentials, authorities);
        this.setAuthenticated(true);
        this.name = name;
    }

    @Override
    public Map<String, Object> getTokenAttributes() {
        return (this.getToken()).getClaims();
    }

    public String getName() {
        return this.name;
    }
}
