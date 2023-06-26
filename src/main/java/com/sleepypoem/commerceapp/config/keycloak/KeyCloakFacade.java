package com.sleepypoem.commerceapp.config.keycloak;

import com.sleepypoem.commerceapp.domain.dto.UserDto;

import java.util.List;

public interface KeyCloakFacade {

    List<UserDto> getAllUsersByCriteria(String criteriaName, String criteriaValue, Integer firstResult, Integer maxResults, boolean exact);

    Long countUsersByCriteria(String criteriaName, String criteriaValue);

    UserDto getUserByUserName(String username);

    UserDto createUser(UserDto userDto);

    UserDto getUserById(String id);

    UserDto updateUser(String userId, UserDto userDto);

    boolean deleteUser(String id);
}
