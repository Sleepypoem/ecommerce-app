package com.sleepypoem.commerceapp.config.security;

import com.sleepypoem.commerceapp.domain.enums.Role;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(@NotNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = extractResourceRoles(jwt);
        CustomPrincipal customPrincipal = new CustomPrincipal();
        customPrincipal.setId(getPrincipalClaimName(jwt));
        customPrincipal.setUsername(jwt.getClaimAsString("preferred_username"));
        customPrincipal.setAuthorities(authorities);
        customPrincipal.setEmail(jwt.getClaimAsString("email"));
        customPrincipal.setFirstName(jwt.getClaimAsString("given_name"));
        customPrincipal.setLastName(jwt.getClaimAsString("family_name"));
        return new CustomJwtAuthToken(jwt, customPrincipal, "JWT", authorities);
    }

    private String getPrincipalClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;
        return jwt.getClaim(claimName);
    }

    private Collection<GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim("realm_access");
        List<String> roles = extractValidRoles(resourceAccess.get("roles") == null ? new ArrayList<>() : (List<String>) resourceAccess.get("roles"));
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList());
    }

    public static List<String> extractValidRoles(List<String> roles) {
        List<String> validRoles = new ArrayList<>();

        for (String role : roles) {
            try {
                role = role.toUpperCase();
                Role.valueOf(role); // Lanza una excepción si el valor no está en el Enum
                validRoles.add(role);
            } catch (IllegalArgumentException ignored) {
                log.info("Ignored role: {}", role);
            }
        }

        return validRoles;
    }
}