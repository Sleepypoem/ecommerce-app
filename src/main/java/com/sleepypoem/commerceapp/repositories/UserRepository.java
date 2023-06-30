package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.domain.dto.UserDto;

import java.util.List;

public interface UserRepository {

    UserDto create(UserDto userDto);

    UserDto update(String userId, UserDto userDto);

    UserDto getUserById(String id);

    UserDto getUserByUserName(String username);

    boolean deleteUser(String id);

    List<UserDto> getAllUsersByCriteria(String criteriaName, String criteriaValue, Integer firstResult, Integer maxResults, boolean exact);

}
