package com.sleepypoem.commerceapp.repositories;

import com.sleepypoem.commerceapp.config.keycloak.KeyCloakFacade;
import com.sleepypoem.commerceapp.controllers.RequestPreconditions;
import com.sleepypoem.commerceapp.controllers.utils.Paginator;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final KeyCloakFacade keyCloakFacade;

    public UserRepositoryImpl(KeyCloakFacade keyCloakFacade) {
        this.keyCloakFacade = keyCloakFacade;
    }

    @Override
    public UserDto create(UserDto userDto) {
        RequestPreconditions.checkRequestElementNotNull(userDto, "UserDto passed to create method is null");
        return keyCloakFacade.createUser(userDto);
    }

    @Override
    public UserDto update(String userId, UserDto userDto) {
        RequestPreconditions.checkRequestElementNotNull(userDto, "UserDto passed to update method is null");
        RequestPreconditions.checkRequestState(userId.equals(userDto.getId()), "User id passed to update method is not equal to user id in UserDto");
        return keyCloakFacade.updateUser(userId, userDto);
    }

    @Override
    public UserDto getUserById(String id) {
        return keyCloakFacade.getUserById(id);
    }

    @Override
    public UserDto getUserByUserName(String username) {
        return keyCloakFacade.getUserByUserName(username);
    }

    @Override
    public boolean deleteUser(String id) {
        return keyCloakFacade.deleteUser(id);
    }

    @Override
    public List<UserDto> getAllUsersByCriteria(String criteriaName, String criteriaValue, Integer firstResult, Integer maxResults, boolean exact) {
        return keyCloakFacade.getAllUsersByCriteria(criteriaName, criteriaValue, firstResult, maxResults, exact);
    }

    public PaginatedDto<UserDto> getAllUsersByCriteriaPaginatedAndSorted(String criteriaName, String criteriaValue, Integer page, Integer size, boolean exact) {
        List<UserDto> result = keyCloakFacade.getAllUsersByCriteria(criteriaName, criteriaValue, page, size, exact);
        Long total = keyCloakFacade.countUsersByCriteria(criteriaName, criteriaValue);
        Paginator<UserDto> paginator = new Paginator<>("users?criteriaName=" + criteriaName + "&criteriaValue=" + criteriaValue);
        return paginator.getPaginatedDtoFromList(page, size, total, result);
    }
}
