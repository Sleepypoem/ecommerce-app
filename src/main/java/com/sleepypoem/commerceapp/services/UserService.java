package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getOneByUserName(String userName) throws MyUserNotFoundException {
        return userRepository.getUserByUserName(userName);
    }

    public UserDto getOneById(String id) throws MyUserNotFoundException {
        return userRepository.getUserById(id);
    }

    public String create(UserDto user) {
        return userRepository.create(user).getId();
    }

    public boolean delete(String id) {
        return userRepository.deleteUser(id);
    }

    public UserDto update(String id, UserDto user) throws MyUserNotFoundException {
        return userRepository.update(id, user);
    }

    public List<UserDto> getAllByCriteria(String criteriaName, String criteriaValue, Integer firstResult, Integer maxResults, boolean exact) {
        return userRepository.getAllUsersByCriteria(criteriaName, criteriaValue, firstResult, maxResults, exact);
    }

}
