package com.sleepypoem.commerceapp.config.keycloak;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sleepypoem.commerceapp.config.beans.AuthServerPropertyLoader;
import com.sleepypoem.commerceapp.domain.dto.KeycloakErrorDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class KeyCloakFacadeImpl implements KeyCloakFacade {

private static final String USERS_ENDPOINT = "/users/";

    private final ObjectMapper mapper;

    private final String adminRestPrefix;

    private final String realmName;

    private final KeyCloakAuthorizer keyCloakAuthorizer;

    private final RequestHelper requestHelper;

    public KeyCloakFacadeImpl(ObjectMapper mapper,
                              AuthServerPropertyLoader authServerPropertyLoader,
                              KeyCloakAuthorizer keyCloakAuthorizer,
                              RequestHelper requestHelper) {
        this.mapper = mapper;
        this.realmName = authServerPropertyLoader.getRealmName();
        this.adminRestPrefix = authServerPropertyLoader.getAdminRestPrefix();
        this.keyCloakAuthorizer = keyCloakAuthorizer;
        this.requestHelper = requestHelper;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
        log.info("Attempting to update user with id: " + userId);
        HttpHeaders headers = requestHelper.createHeaders(MediaType.APPLICATION_JSON, keyCloakAuthorizer.getAccessToken());

        HttpEntity<String> request = requestHelper.createHttpEntity(userDto.toJsonString(), headers);
        requestHelper.makeRequest(adminRestPrefix + realmName + USERS_ENDPOINT + userId, HttpMethod.PUT, request);
        return userDto;
    }


    @Override
    public boolean deleteUser(String id) {
        log.info("Attempting to deleteById user with id: " + id);
        HttpHeaders headers = requestHelper.createHeaders(MediaType.APPLICATION_JSON, keyCloakAuthorizer.getAccessToken());

        try {
            requestHelper.makeRequest(adminRestPrefix + realmName + USERS_ENDPOINT + id,
                    HttpMethod.DELETE,
                    requestHelper.createEmptyHttpEntity(headers));
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public UserDto getUserByUserName(String username) {
        log.info("Attempting to retrieve user with username: " + username);
        HttpHeaders headers = requestHelper.createHeaders(MediaType.APPLICATION_JSON, keyCloakAuthorizer.getAccessToken());

        HttpEntity<String> entity = requestHelper.createEmptyHttpEntity(headers);
        ResponseEntity<String> response = requestHelper.makeRequest(
                adminRestPrefix + realmName + "/users?username=" + username + "&exact=true", HttpMethod.GET, entity);
        List<UserDto> foundUsers = mapToUserDtoList(response.getBody());
        if (foundUsers.isEmpty()) {
            KeycloakErrorDto keycloakErrorDto = new KeycloakErrorDto();
            keycloakErrorDto.setError("User not found");
            throw new MyUserNotFoundException("User with username: " + username + ", not found", keycloakErrorDto);
        }
        return foundUsers.get(0);
    }

    @Override
    public UserDto createUser(UserDto user) {
        log.info("Attempting to add user: " + user.getUsername());
        HttpHeaders headers = requestHelper.createHeaders(MediaType.APPLICATION_JSON, keyCloakAuthorizer.getAccessToken());

        HttpEntity<String> request = requestHelper.createHttpEntity(user.toJsonString(), headers);
        requestHelper.makeRequest(adminRestPrefix + realmName + USERS_ENDPOINT, HttpMethod.POST, request);

        return user;
    }

    private List<UserDto> mapToUserDtoList(String response) {
        if(response == null || response.equals("[]")) {
            return Collections.emptyList();
        }

        try {
            return mapper.readValue(response, mapper.getTypeFactory().constructCollectionType(List.class, UserDto.class));
        } catch (JsonProcessingException e) {
            throw new MyInternalException("Error while parsing user list from keycloak server", e);
        }
    }

    @Override
    public UserDto getUserById(String id) {
        log.info("Attempting to retrieve user by id: " + id);
        HttpHeaders headers = requestHelper.createHeaders(MediaType.APPLICATION_JSON, keyCloakAuthorizer.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        ResponseEntity<String> response = requestHelper.makeRequest(adminRestPrefix + realmName + USERS_ENDPOINT + id,
                HttpMethod.GET,
                requestHelper.createEmptyHttpEntity(headers)
        );

        return UserDto.fromJsonString(response.getBody());
    }

    @Override
    public List<UserDto> getAllUsersByCriteria(String criteriaName, String criteriaValue, Integer firstResult, Integer maxResults, boolean exact) {
        log.info("Attempting to retrieve all users by criteria: " + criteriaName + " with value: " + criteriaValue);
        checkCriteriaNotNull(criteriaName, criteriaValue);
        HttpHeaders headers = requestHelper.createHeaders(MediaType.APPLICATION_JSON, keyCloakAuthorizer.getAccessToken());
        HttpEntity<String> entity = requestHelper.createEmptyHttpEntity(headers);
        ResponseEntity<String> response = requestHelper.makeRequest(createPaginatedRequestUrl(criteriaName, criteriaValue, firstResult, maxResults, exact),
                HttpMethod.GET, entity);

        return mapToUserDtoList(response.getBody());

    }

    private void checkCriteriaNotNull(String criteriaName, String criteriaValue) {
        try {
            Objects.requireNonNull(criteriaName, "Criteria name cannot be null");
            Objects.requireNonNull(criteriaValue, "Criteria value cannot be null");
        } catch (NullPointerException e) {
            throw new MyBadRequestException(e.getMessage());
        }
    }

    private String createPaginatedRequestUrl(String criteriaName, String criteriaValue, Integer page, Integer size, Boolean exact) {
        return adminRestPrefix + realmName + "/users?" + criteriaName + "=" + criteriaValue + "&first=" + page + "&max=" + size + "&exact=" + exact;
    }

    @Override
    public Long countUsersByCriteria(String criteriaName, String criteriaValue) {
        log.info("Attempting to count users by criteria: " + criteriaName + " with value: " + criteriaValue);
        checkCriteriaNotNull(criteriaName, criteriaValue);
        HttpHeaders headers = requestHelper.createHeaders(MediaType.APPLICATION_JSON, keyCloakAuthorizer.getAccessToken());
        HttpEntity<String> entity = requestHelper.createEmptyHttpEntity(headers);
        ResponseEntity<String> response = requestHelper.makeRequest(adminRestPrefix + realmName + "/users/count?" + criteriaName + "=" + criteriaValue,
                HttpMethod.GET, entity);
        return response.getBody() == null ? 0L : Long.parseLong(response.getBody());
    }
}
