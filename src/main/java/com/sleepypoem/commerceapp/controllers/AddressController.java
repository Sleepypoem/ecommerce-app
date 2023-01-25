package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.config.authentication.AuthenticationFacade;
import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.AddressMapper;
import com.sleepypoem.commerceapp.services.AddressService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("addresses")
@Slf4j
public class AddressController extends AbstractController<AddressDto, AddressEntity> {

    @Autowired
    AddressService service;

    @Autowired
    AddressMapper mapper;

    @Autowired
    AuthenticationFacade authentication;

    @Override
    protected AbstractService<AddressDto, AddressEntity> getService() {
        return service;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<ResourceStatusResponseDto> create(@RequestBody AddressEntity address) throws Exception {
        String userId = authentication.getAuthentication().getName();
        log.info(authentication.getAuthentication().getAuthorities().toString());
        address.setUserId(userId);
        AddressDto created = createInternal(address);
        String message = "Address created with id " + created.getId();
        String url = "GET : /api/addresses/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/address/" + address.getId()))
                .body(new ResourceStatusResponseDto(String.valueOf(created.getId()), message, url));
    }

    @PutMapping("{id}")
    public ResponseEntity<ResourceStatusResponseDto> update(@PathVariable Long id, @RequestBody AddressEntity address) throws Exception {
        AddressDto updated = updateInternal(id, address);
        String message = updated.toString();
        String url = "GET : /api/addresses/" + updated.getId();
        return ResponseEntity.ok(new ResourceStatusResponseDto(String.valueOf(updated.getId()), message, url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getOneById(@PathVariable Long id) {
        AddressDto searchedAddress = getOneByIdInternal(id);
        return ResponseEntity.ok().body(searchedAddress);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {

        boolean deleted = deleteInternal(id);
        if(deleted) {
            return ResponseEntity.ok().body("Address with id " + id + " was deleted.");
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getByUserId(@RequestParam(value = "user-id") String userId) {
        List<AddressEntity> addresses = service.findByUserId(userId);
        return ResponseEntity.ok().body(mapper.convertToDtoList(addresses));
    }
}
