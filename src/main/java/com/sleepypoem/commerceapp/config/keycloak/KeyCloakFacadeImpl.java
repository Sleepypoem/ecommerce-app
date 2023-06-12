package com.sleepypoem.commerceapp.config.keycloak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepypoem.commerceapp.config.beans.AuthServerPropertyLoader;
import com.sleepypoem.commerceapp.domain.dto.KeycloakErrorDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.dto.UserRepresentationDto;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Slf4j
public class KeyCloakFacadeImpl implements KeyCloakFacade {

private static final String USERS_ENDPOINT = "/users/";

    private final ObjectMapper mapper;

    private final String adminRestPrefix;

    private final String realmName;

    private final KeycloakAuthorizerImpl keycloakAuthorizerImpl;

    public KeyCloakFacadeImpl(ObjectMapper mapper, AuthServerPropertyLoader authServerPropertyLoader, KeycloakAuthorizerImpl keycloakAuthorizerImpl) {
        this.mapper = mapper;
        this.realmName = authServerPropertyLoader.getRealmName();
        this.adminRestPrefix = authServerPropertyLoader.getAdminRestPrefix();
        this.keycloakAuthorizerImpl = keycloakAuthorizerImpl;
    }

    @Override
    public UserDto updateUser(String userId, UserRepresentationDto userRepresentationDto) {
        log.info("Attempting to update user with id: " + userId);
        HttpHeaders headers = RequestCommons.createHeaders(MediaType.APPLICATION_JSON, keycloakAuthorizerImpl.getAccessToken());

        HttpEntity<String> request = new HttpEntity<>(userRepresentationDto.toJsonString(), headers);
        RequestCommons.makeRequest(adminRestPrefix + realmName + USERS_ENDPOINT + userId, HttpMethod.PUT, request);
        return getUserById(userId);
    }


    @Override
    public void deleteUser(String id) {
        log.info("Attempting to delete user with id: " + id);
        HttpHeaders headers = RequestCommons.createHeaders(MediaType.APPLICATION_JSON, keycloakAuthorizerImpl.getAccessToken());

        RequestCommons.makeRequest(adminRestPrefix + realmName + USERS_ENDPOINT + id,
                HttpMethod.DELETE,
                RequestCommons.createEmptyHttpEntity(headers));
    }

    @Override
    public UserDto getUserByUserName(String username) {
        log.info("Attempting to retrieve user with username: " + username);
        HttpHeaders headers = RequestCommons.createHeaders(MediaType.APPLICATION_JSON, keycloakAuthorizerImpl.getAccessToken());

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = RequestCommons.makeRequest(
                adminRestPrefix + realmName + "/users?username=" + username + "&exact=true", HttpMethod.GET, entity);
        if ("[]".equals(response.getBody())) {
            KeycloakErrorDto keycloakErrorDto = new KeycloakErrorDto();
            keycloakErrorDto.setError("User not found");
            throw new MyUserNotFoundException("User with username: " + username + " not found", keycloakErrorDto);
        }
        return mapToUserDtoList(response.getBody()).get(0);
    }

    @Override
    public UserDto createUser(UserRepresentationDto user) {
        log.info("Attempting to add user: " + user.getUsername());
        HttpHeaders headers = RequestCommons.createHeaders(MediaType.APPLICATION_JSON, keycloakAuthorizerImpl.getAccessToken());

        HttpEntity<String> request = new HttpEntity<>(user.toJsonString(), headers);
        RequestCommons.makeRequest(adminRestPrefix + realmName + USERS_ENDPOINT, HttpMethod.POST, request);

        return getUserByUserName(user.getUsername());
    }

    private List<UserDto> mapToUserDtoList(String response) {
        try {
            return mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, UserDto.class));
        } catch (JsonProcessingException e) {
            throw new MyInternalException("Error while parsing user list from keycloak server", e);
        }
    }

    @Override
    public UserDto getUserById(String id) {
        log.info("Attempting to retrieve user by id: " + id);
        HttpHeaders headers = RequestCommons.createHeaders(MediaType.APPLICATION_JSON, keycloakAuthorizerImpl.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        ResponseEntity<String> response = RequestCommons.makeRequest(adminRestPrefix + realmName + USERS_ENDPOINT + id,
                HttpMethod.GET,
                RequestCommons.createEmptyHttpEntity(headers)
        );

        return UserDto.fromJsonString(response.getBody());
    }

    @Override
    public List<UserDto> getAllUsersByCriteria(String criteriaName, String criteriaValue, Integer firstResult, Integer maxResults, boolean exact) {
        log.info("Attempting to retrieve all users by criteria: " + criteriaName + " with value: " + criteriaValue);
        HttpHeaders headers = RequestCommons.createHeaders(MediaType.APPLICATION_JSON, keycloakAuthorizerImpl.getAccessToken());
        HttpEntity<String> entity = RequestCommons.createEmptyHttpEntity(headers);
        ResponseEntity<String> response = RequestCommons.makeRequest(createPaginatedRequest(criteriaName, criteriaValue, firstResult, maxResults, exact),
                HttpMethod.GET, entity);

        return mapToUserDtoList(response.getBody());

    }

    private String createPaginatedRequest(String criteriaName, String criteriaValue, Integer page, Integer size, Boolean exact) {
        return adminRestPrefix + realmName + "/users?" + criteriaName + "=" + criteriaValue + "&first=" + page + "&max=" + size + "&exact=" + exact;
    }

    @Override
    public Long countUsersByCriteria(String criteriaName, String criteriaValue) {
        return null;
    }
}
