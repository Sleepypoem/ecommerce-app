package com.sleepypoem.commerceapp.domain.dto.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ApiError {

    private int status;
    private String message;
    private String developerMessage;
}
