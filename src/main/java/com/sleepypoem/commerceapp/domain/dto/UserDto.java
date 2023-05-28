package com.sleepypoem.commerceapp.domain.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserDto {

    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Collection<GrantedAuthority> authorities;
}
