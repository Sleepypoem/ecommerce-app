package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.annotations.security.IsAdminOrSuperUser;
import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.controllers.utils.Paginator;
import com.sleepypoem.commerceapp.controllers.utils.SecurityUtils;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.entities.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutItemMapper;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.services.CheckoutService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("checkouts")
@Slf4j
public class CheckoutController extends AbstractController<CheckoutDto, CheckoutEntity, Long> {

    private final CheckoutService service;

    protected CheckoutController(BaseMapper<CheckoutEntity, CheckoutDto> mapper, CheckoutService service) {
        super(mapper);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResourceStatusResponseDto> create(@RequestBody CheckoutEntity checkout) {
        checkout.setUserId(SecurityUtils.getCurrentLoggedUserId());
        CheckoutDto created = createInternal(checkout);
        String message = "Checkout created with id " + created.getId();
        String url = "GET : /api/checkouts/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/checkout/" + created.getId()))
                .body(new ResourceStatusResponseDto(String.valueOf(created.getId()), message, url));
    }

    @PutMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<CheckoutDto> update(@PathVariable Long id, @RequestBody CheckoutEntity checkout) {
        checkout.setUserId(SecurityUtils.getCurrentLoggedUserId());
        return ResponseEntity.ok().body(updateInternal(id, checkout));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or @checkoutService.getOneById(#id).userId == authentication.principal.id")
    public ResponseEntity<ResourceStatusResponseDto> delete(@PathVariable Long id) {
        boolean deleted = service.deleteById(id);
        if(deleted) {
            String message = "Checkout with id " + id + " deleted successfully";
            return ResponseEntity.ok().body(new ResourceStatusResponseDto(String.valueOf(id), message, null));
        }else {
            String message = "Error deleting checkout with id " + id;
            return ResponseEntity.internalServerError().body(new ResourceStatusResponseDto(String.valueOf(id), message, null));
        }
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<CheckoutDto> getOneById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @GetMapping(params = {"user-id"}, produces = "application/json")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or #userId == authentication.principal.id")
    public ResponseEntity<PaginatedDto<CheckoutDto>> getByUserIdPaginatedAndSorted(@RequestParam(value = "user-id") String userId,
                                                                                   @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                   @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                   @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                                                   @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        Paginator<CheckoutDto> paginator = new Paginator<>("checkouts?userId=" + userId + "&");
        return ResponseEntity.ok().body(paginator.getPaginatedDtoFromPage(service.getAllPaginatedAndSortedByUserId(userId, page, size, sortBy, sortOrder), mapper));
    }

    @GetMapping
    @IsAdminOrSuperUser
    public ResponseEntity<PaginatedDto<CheckoutDto>> getAllPaginatedAndSorted(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                              @RequestParam(value = "size", defaultValue = "10") int size,
                                                                              @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                                              @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        return ResponseEntity.ok().body(getAllPaginatedAndSortedInternal(page, size, sortBy, sortOrder, "checkouts?"));
    }

    @PostMapping("/{id}/items")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<CheckoutDto> addItemsToCart(@PathVariable Long id, @RequestBody List<CheckoutItemEntity> items) {
        if(items == null || items.isEmpty()) {
            throw new MyBadRequestException("Items list cannot be empty");
        }
        return ResponseEntity.ok().body(mapper.convertToDto(service.addItems(id, items)));
    }

    @DeleteMapping("/{id}/items/{item-id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or @checkoutService.getOneById(#id).userId == authentication.principal.id")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable("id") Long id,
                                                   @PathVariable("item-id") Long itemId) {
        service.removeItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/items/{item-id}")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<CheckoutDto> modifyQuantity(@PathVariable("id") Long id,
                                                      @PathVariable("item-id") Long itemId,
                                                      @RequestBody int quantity) {
        service.modifyItemQuantity(id, itemId, quantity);
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @GetMapping("/{id}/items")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or @checkoutService.getOneById(#id).userId == authentication.principal.id")
    public ResponseEntity<PaginatedDto<CheckoutItemDto>> getItemsPaginatedAndSorted(@PathVariable Long id,
                                                                                    @RequestParam(value = "page", defaultValue = "0") int page,
                                                                                    @RequestParam(value = "size", defaultValue = "10") int size,
                                                                                    @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                                                    @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        Paginator<CheckoutItemDto> paginator = new Paginator<>("checkouts/" + id + "/items?");
        Page<CheckoutItemEntity> pagedItems = service.getAllItemsPaginatedAndSorted(id, page, size, sortBy, sortOrder);
        return ResponseEntity.ok().body(paginator.getPaginatedDtoFromPage(pagedItems, new CheckoutItemMapper()));
    }

    @PostMapping("/{id}/address")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<CheckoutDto> addPreferredAddress(@PathVariable Long id, @RequestBody AddressEntity address) {
        return ResponseEntity.ok().body(mapper.convertToDto(service.addPreferredAddress(id, address)));
    }

    @PostMapping("/{id}/payment-method")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<CheckoutDto> addPreferredPaymentMethod(@PathVariable Long id, @RequestBody PaymentMethodEntity paymentMethod) {
        return ResponseEntity.ok().body(mapper.convertToDto(service.addPreferredPaymentMethod(id, paymentMethod)));
    }

    @Override
    public AbstractService<CheckoutEntity, Long> getService() {
        return service;
    }
}
