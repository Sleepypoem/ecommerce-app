package com.sleepypoem.signinapp.payload.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatusCode;

import java.util.Map;

@AllArgsConstructor
@Getter
@ToString
public class RequestFieldError {
    private HttpStatusCode status;
    private String message;
    private final Map<String, String> errors;

}
