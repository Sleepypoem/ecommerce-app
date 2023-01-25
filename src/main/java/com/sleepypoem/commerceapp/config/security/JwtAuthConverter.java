package com.sleepypoem.commerceapp.config.security;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.services.helpers.UserResourceBinder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    static final String FIRST_NAME_CLAIM = "given_name";
    static final String LAST_NAME_CLAIM = "family_name";
    static final String USERNAME_CLAIM = "preferred_username";
    static final String EMAIL_CLAIM = "email";

    static final String ROLES_CLAIM = "realm_access";

    @Autowired
    UserResourceBinder binder;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    private final JwtAuthConverterProperties properties;

    public JwtAuthConverter(JwtAuthConverterProperties properties) {
        this.properties = properties;
    }

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {

        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()).collect(Collectors.toSet());
        String principalClaimValue = getPrincipalClaimName(jwt);

        UserDto principal = UserDto
                .builder()
                .id(principalClaimValue)
                .firstName(getPrincipalAttributeString(jwt, FIRST_NAME_CLAIM))
                .lastName(getPrincipalAttributeString(jwt, LAST_NAME_CLAIM))
                .username(getPrincipalAttributeString(jwt, USERNAME_CLAIM))
                .email(getPrincipalAttributeString(jwt, EMAIL_CLAIM))
                .roles(authorities)
                .build();

        binder.attachAddresses(principal);
        binder.attachCheckout(principal);
        binder.attachPaymentMethods(principal);

        return new UserToken(jwt, principal,principalClaimValue,  authorities, getPrincipalClaimName(jwt));
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        if (properties.getPrincipalAttribute() != null) {
            claimName = properties.getPrincipalAttribute();
        }
        return jwt.getClaim(claimName);
    }

    private String getPrincipalAttributeString(Jwt jwt, String attr) {
        String claim = null;
        if (attr != null) {
            claim = jwt.getClaim(attr);
        }
        return claim;
    }


    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim(ROLES_CLAIM);

        List<String> roles= (List<String>) resourceAccess.get("roles");

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }
}