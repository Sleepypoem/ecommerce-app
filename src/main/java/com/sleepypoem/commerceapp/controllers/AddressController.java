package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.annotations.security.IsAdminOrSuperUser;
import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.controllers.utils.Paginator;
import com.sleepypoem.commerceapp.controllers.utils.SecurityUtils;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.AddressDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.services.AddressService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("addresses")
@Slf4j
public class AddressController extends AbstractController<AddressDto, AddressEntity, Long> {

    AddressService service;

    protected AddressController(BaseMapper<AddressEntity, AddressDto> mapper, AddressService service) {
        super(mapper);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResourceStatusResponseDto> create(@RequestBody AddressEntity address) {
        address.setUserId(SecurityUtils.getCurrentLoggedUserId());
        AddressDto created = createInternal(address);
        String message = "Address created with id " + created.getId();
        String url = "GET : /api/addresses/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/address/" + address.getId()))
                .body(new ResourceStatusResponseDto(String.valueOf(created.getId()), message, url));
    }

    @PutMapping("{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or #address.userId == principal.id")
    public ResponseEntity<AddressDto> update(@PathVariable Long id, @RequestBody AddressEntity address) {
        address.setUserId(SecurityUtils.getCurrentLoggedUserId());
        return ResponseEntity.ok(updateInternal(id, address));
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or returnObject.body.userId == principal.id")
    public ResponseEntity<AddressDto> getOneById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @GetMapping(params = {"user-id"}, produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or #userId == principal.id")
    public ResponseEntity<PaginatedDto<AddressDto>> getByUserIdPaginatedAndSorted(@RequestParam(value = "user-id") String userId,
                                                                                  @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                  @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                                                  @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        Paginator<AddressDto> paginator = new Paginator<>("addresses?user-id=" + userId + "&");
        return ResponseEntity.ok().body(paginator.getPaginatedDtoFromPage(service.findByUserId(userId, page, size, sortBy, sortOrder), mapper));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or @addressService.getOneById(#id).userId == principal.id")
    public ResponseEntity<ResourceStatusResponseDto> delete(@PathVariable Long id) {
        boolean deleted = deleteInternal(id);
        if (deleted) {
            String message = "Address deleted with id " + id;
            return ResponseEntity.ok().body(new ResourceStatusResponseDto(String.valueOf(id), message, null));
        } else {
            String message = "Error deleting address with id " + id;
            return ResponseEntity.internalServerError().body(new ResourceStatusResponseDto(String.valueOf(id), message, null));
        }
    }

    @GetMapping(produces = "application/json")
    @IsAdminOrSuperUser
    public ResponseEntity<PaginatedDto<AddressDto>> getAllPaginatedAndSorted(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                             @RequestParam(value = "size", defaultValue = "10") int size,
                                                                             @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                                             @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok().body(getAllPaginatedAndSortedInternal(page, size, sortBy, sortOrder, "addresses?"));
    }

    @Override
    public AbstractService<AddressEntity, Long> getService() {
        return service;
    }
}
