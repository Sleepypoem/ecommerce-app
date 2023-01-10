package com.sleepypoem.commerceapp.domain.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CredentialsDto {

    private String type;
    private String value;
    private String temporary;
}
