package com.sleepypoem.commerceapp.services;

import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.dto.UserRepresentationDto;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDto getUserByUserName(String userName) throws MyUserNotFoundException {
        return userRepository.getUserByUserName(userName);
    }

    public UserDto getUserById(String id) throws MyUserNotFoundException {
        return userRepository.getUserById(id);
    }

    public String addUser(UserRepresentationDto user) {
        return userRepository.create(user).getId();
    }

    public void deleteUser(String id) {
        userRepository.deleteUser(id);
    }

    public UserDto updateUser(String id, UserRepresentationDto user) throws MyUserNotFoundException {
        return userRepository.update(id, user);
    }

}
