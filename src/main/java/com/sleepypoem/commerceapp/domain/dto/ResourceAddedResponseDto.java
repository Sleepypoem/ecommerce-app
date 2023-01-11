package com.sleepypoem.commerceapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResourceAddedResponseDto {

    private String id;
    private String message;
    private String url;
}
