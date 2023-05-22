package com.sleepypoem.commerceapp.domain.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserRepresentationDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String enabled = "true";
    private String emailVerified = "true";
    private List<CredentialsDto> credentials;
}
