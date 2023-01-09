package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;
import com.sleepypoem.commerceapp.services.AddressService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("address")
public class AddressController extends AbstractController<AddressDto, AddressEntity> {

    @Autowired
    AddressService service;

    @Override
    protected AbstractService<AddressDto, AddressEntity> getService() {
        return service;
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getAll() {
        return ResponseEntity.ok().body(getAllInternal());
    }

    @PostMapping
    public ResponseEntity<AddressDto> create(@RequestBody AddressEntity address) throws MyValidationException {
        return ResponseEntity.created(URI.create("/address")).body(createInternal(address));
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressDto> update(@PathVariable Long id, @RequestBody AddressEntity address) throws MyValidationException {
        return ResponseEntity.ok(updateInternal(id, address));
    }
}
