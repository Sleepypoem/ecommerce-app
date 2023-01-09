package com.sleepypoem.commerceapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.dto.UserRepresentationDto;
import com.sleepypoem.commerceapp.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("users")
@Slf4j
public class UserController {

    @Autowired
    UserService service;

    @PostMapping(produces = {"application/json"})
    public ResponseEntity<String> createUser(@RequestBody UserRepresentationDto user) throws Exception {
        String userId = service.addUser(user);
        return ResponseEntity.created(URI.create("/users")).body("{\"user-id\" : \"" + userId + "\"}");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) throws Exception {
        return ResponseEntity.ok().body(service.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<UserDto> getUserByUserName(@RequestParam(value = "username") String username) throws Exception {
        return ResponseEntity.ok().body(service.getUserByUserName(username));
    }
}
