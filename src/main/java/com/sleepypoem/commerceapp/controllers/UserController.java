package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.exceptions.MyUserNotFoundException;
import com.sleepypoem.commerceapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("users")
@Slf4j
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping(produces = {"application/json"})
    public ResponseEntity<ResourceStatusResponseDto> createUser(@RequestBody UserDto user) {
        String userId = service.create(user);
        String message = "User created with id " + userId;
        String url = "GET : /api/users/" + userId;
        return ResponseEntity
                .created(URI.create("/api/users/" + userId))
                .body(new ResourceStatusResponseDto(userId, message, url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) throws MyUserNotFoundException {
        return ResponseEntity.ok().body(service.getOneById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResourceStatusResponseDto> deleteUserById(@PathVariable String id) throws MyUserNotFoundException {
        service.delete(id);
        String message = "User deleted with id " + id;
        return ResponseEntity.ok().body(new ResourceStatusResponseDto(id, message, null));
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserByUserName(@RequestParam(value = "username") String username) {
        return ResponseEntity.ok().body(service.getOneByUserName(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody UserDto user) throws MyUserNotFoundException {
        return ResponseEntity.ok().body(service.update(id, user));
    }
}
