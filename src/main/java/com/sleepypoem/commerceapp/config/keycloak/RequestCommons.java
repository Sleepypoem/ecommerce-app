package com.sleepypoem.commerceapp.config.keycloak;

import com.sleepypoem.commerceapp.domain.dto.KeycloakErrorDto;
import com.sleepypoem.commerceapp.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Component
@Slf4j
public class RequestCommons {

    private static final WebClient webClient = WebClient.create();

    private static final int UNAUTHORIZED = 401;

    private static final int BAD_REQUEST = 400;

    private static final int NOT_FOUND = 404;

    private static final int CONFLICT = 409;


    private RequestCommons() {
    }

    public static HttpHeaders createHeaders(MediaType mediaType, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        if (token != null) {
            headers.add("Authorization", "Bearer " + token);
        }

        return headers;
    }
    @SuppressWarnings("all")
    public static ResponseEntity<String> makeRequest(String url, HttpMethod method, HttpEntity<?> entity) {
        //suppressed warnings in the following block because i'm new at working with WebClient
        // and i'm not sure how to fix them, when i learn more i'll come back and fix this
        log.info("Making request to: " + url);

        ResponseEntity<String> response = webClient.method(method)
                .uri(url)
                .body(entity.getBody() == null ? BodyInserters.empty() : BodyInserters.fromValue(entity.getBody()))
                .headers(headers -> headers.putAll(entity.getHeaders()))
                .exchange()
                .block()
                .toEntity(String.class)
                .block();
        assert response != null;
        evaluateStatusCode(response);
        return response;
    }

    private static void evaluateStatusCode(ResponseEntity<String> response) {
        log.info("Evaluating request response... Response status code: " + response.getStatusCode());
        int statusCode = response.getStatusCode().value();
        String responseBody = response.getBody();

        if (responseBody == null) {
            throw new MyInternalException("Response from authorization server is null.");
        }
        KeycloakErrorDto errorDto = KeycloakErrorDto.fromJsonString(responseBody);

        if (statusCode != OK.value() && statusCode != CREATED.value()) {
            switch (statusCode) {
                case UNAUTHORIZED -> throw new MyAuthServerException("Unauthorized, check your credentials.", errorDto);
                case BAD_REQUEST -> throw new MyBadRequestException("Bad request, check your request body.", errorDto);
                case NOT_FOUND -> {
                    assert errorDto != null;
                    if (errorDto.getError() != null && errorDto.getError().contains("User not found")) {
                        throw new MyUserNotFoundException("User not found.", errorDto);
                    } else {
                        throw new MyAuthServerException("Error in auth server url.", errorDto);
                    }
                }
                case CONFLICT -> throw new MyUserNameAlreadyUsedException("Username already used.", errorDto);
                default -> throw new MyAuthServerException("Error while connecting with auth server", errorDto);
            }
        }
    }

    public static <T> HttpEntity<T> createHttpEntity(T body, HttpHeaders headers) {
        if (body == null) {
            return new HttpEntity<>(null, headers);
        } else {
            return new HttpEntity<>(body, headers);
        }
    }

    public static <T> HttpEntity<T> createEmptyHttpEntity(HttpHeaders headers) {
        return createHttpEntity(null, headers);
    }

}
