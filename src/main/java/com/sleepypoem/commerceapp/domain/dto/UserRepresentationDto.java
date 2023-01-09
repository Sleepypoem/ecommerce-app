package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRepresentationDto {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String enabled = "true";
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<CredentialsDto> credentials;
}
