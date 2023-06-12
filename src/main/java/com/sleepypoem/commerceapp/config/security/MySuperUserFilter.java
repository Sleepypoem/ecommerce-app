package com.sleepypoem.commerceapp.config.security;


import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import com.sleepypoem.commerceapp.config.beans.AuthServerPropertyLoader;
import com.sleepypoem.commerceapp.config.beans.SecurityContextProvider;
import com.sleepypoem.commerceapp.domain.enums.Role;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class MySuperUserFilter implements Filter {

    private final String adminUserName;

    public MySuperUserFilter(AuthServerPropertyLoader authServerPropertyLoader) {
        this.adminUserName = authServerPropertyLoader.getAdminUsername();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("Entering custom filter...");
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = httpRequest.getHeader("Authorization");

        //Username in keycloak should be unique, so this is safe
        if (token != null) {
            JWTClaimsSet claims = null;
            try {
                claims = JWTParser.parse(token.substring(7)).getJWTClaimsSet();
            } catch (ParseException e) {
                throw new MyInternalException("Error parsing token");
            }
            if (claims.getClaim("preferred_username").equals(adminUserName)) {
                log.info("Superuser detected! Setting superuser role...");
                CustomPrincipal specialUserPrincipal = (CustomPrincipal) SecurityContextProvider.getSecurityContext().getAuthentication().getPrincipal();
                specialUserPrincipal.setAuthorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + Role.SUPERUSER.name())));
            }

        }

        chain.doFilter(request, response);
    }
}
