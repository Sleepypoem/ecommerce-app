package com.sleepypoem.signinapp.payload.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class ApiError {

    private int status;
    private String message;
    private String developerMessage;
}
