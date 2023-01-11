package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceAddedResponseDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.AddressMapper;
import com.sleepypoem.commerceapp.exceptions.MyResourceNotFoundException;
import com.sleepypoem.commerceapp.services.AddressService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("addresses")
@Slf4j
public class AddressController extends AbstractController<AddressDto, AddressEntity> {

    @Autowired
    AddressService service;

    @Autowired
    AddressMapper mapper;

    @Override
    protected AbstractService<AddressDto, AddressEntity> getService() {
        return service;
    }

    @PostMapping
    public ResponseEntity<ResourceAddedResponseDto> create(@RequestBody AddressEntity address) throws Exception {
        AddressDto created = createInternal(address);
        String message = "Address created with id " + created.getId();
        String url = "GET : /api/addresses/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/address/" + address.getId()))
                .body(new ResourceAddedResponseDto(String.valueOf(created.getId()), message, url));
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressDto> update(@PathVariable Long id, @RequestBody AddressEntity address) throws Exception {
        return ResponseEntity.ok(updateInternal(id, address));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getOneById(@PathVariable Long id) {
        Optional<AddressDto> searchedAddress = getOneByIdInternal(id);
        if (searchedAddress.isEmpty()) {
            throw new MyResourceNotFoundException("Address with id " + id + " not found.");
        }
        return ResponseEntity.ok().body(searchedAddress.get());
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getByUserId(@RequestParam(value = "user-id") String userId) {
        List<AddressEntity> addresses = service.findByUserId(userId);
        return ResponseEntity.ok().body(mapper.convertToDtoList(addresses));
    }
}
