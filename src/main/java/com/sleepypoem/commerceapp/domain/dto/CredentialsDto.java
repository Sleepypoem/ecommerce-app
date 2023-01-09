package com.sleepypoem.commerceapp.domain.dto;

import lombok.Data;

@Data
public class CredentialsDto {

    private String type;
    private String value;
    private String temporary;
}
