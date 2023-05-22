package com.sleepypoem.commerceapp.config.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
public class CustomPrincipal {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Collection<GrantedAuthority> authorities;
}
