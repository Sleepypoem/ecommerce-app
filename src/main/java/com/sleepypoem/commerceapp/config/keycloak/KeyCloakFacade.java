package com.sleepypoem.commerceapp.config.keycloak;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.dto.UserRepresentationDto;

import java.util.List;

public interface KeyCloakFacade {

    List<UserDto> getAllUsersByCriteria(String criteriaName, String criteriaValue, Integer firstResult, Integer maxResults, boolean exact);

    Long countUsersByCriteria(String criteriaName, String criteriaValue);

    UserDto getUserByUserName(String username);

    UserDto createUser(UserRepresentationDto userRepresentationDto);

    UserDto getUserById(String id);

    UserDto updateUser(String userId, UserRepresentationDto userRepresentationDto);

    void deleteUser(String id);
}
