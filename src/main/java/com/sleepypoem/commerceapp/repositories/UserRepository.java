package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.dto.UserRepresentationDto;

import java.util.List;

public interface UserRepository {

    UserDto create(UserRepresentationDto userRepresentationDto);

    UserDto update(String userId, UserRepresentationDto userRepresentationDto);

    UserDto getUserById(String id);

    UserDto getUserByUserName(String username);

    void deleteUser(String id);

    List<UserDto> getAllUsersByCriteria(String criteriaName, String criteriaValue, Integer firstResult, Integer maxResults, boolean exact);

}
