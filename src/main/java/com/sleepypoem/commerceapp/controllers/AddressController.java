package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.AddressDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.services.AddressService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("addresses")
@Slf4j
public class AddressController extends AbstractController<AddressDto, AddressEntity> {

    AddressService service;

    protected AddressController(BaseMapper<AddressEntity, AddressDto> mapper, AddressService service) {
        super(mapper);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResourceStatusResponseDto> create(@RequestBody AddressEntity address) throws Exception {
        AddressDto created = createInternal(address);
        String message = "Address created with id " + created.getId();
        String url = "GET : /api/addresses/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/address/" + address.getId()))
                .body(new ResourceStatusResponseDto(String.valueOf(created.getId()), message, url));
    }

    @PutMapping("{id}")
    public ResponseEntity<AddressDto> update(@PathVariable Long id, @RequestBody AddressEntity address) throws Exception {
        return ResponseEntity.ok(updateInternal(id, address));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AddressDto> getOneById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @GetMapping
    public ResponseEntity<List<AddressDto>> getByUserId(@RequestParam(value = "user-id") String userId) {
        List<AddressEntity> addresses = service.findByUserId(userId);
        return ResponseEntity.ok().body(mapper.convertToDtoList(addresses));
    }

    @Override
    public AbstractService<AddressEntity> getService() {
        return service;
    }
}
