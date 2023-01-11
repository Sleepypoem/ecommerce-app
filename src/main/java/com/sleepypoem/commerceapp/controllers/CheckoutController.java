package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceAddedResponseDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutMapper;
import com.sleepypoem.commerceapp.exceptions.MyResourceNotFoundException;
import com.sleepypoem.commerceapp.services.CheckoutService;
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
@RequestMapping("checkouts")
@Slf4j
public class CheckoutController extends AbstractController<CheckoutDto, CheckoutEntity> {

    @Autowired
    CheckoutService service;

    @Autowired
    CheckoutMapper mapper;

    @Override
    protected AbstractService<CheckoutDto, CheckoutEntity> getService() {
        return service;
    }

    @PostMapping
    public ResponseEntity<ResourceAddedResponseDto> create(@RequestBody CheckoutEntity checkout) throws Exception {
        CheckoutDto created = createInternal(checkout);
        String message = "Checkout created with id " + created.getId();
        String url = "GET : /api/checkouts/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/checkout/" + created.getId()))
                .body(new ResourceAddedResponseDto(String.valueOf(created.getId()), message, url));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckoutDto> update(@PathVariable Long id, @RequestBody CheckoutEntity checkout) throws Exception {
        return ResponseEntity.ok().body(updateInternal(id, checkout));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok().body("Checkout with id " + " id has been deleted.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckoutDto> findOneById(@PathVariable Long id) {
        Optional<CheckoutDto> searched = getOneByIdInternal(id);
        if (searched.isEmpty()) {
            throw new MyResourceNotFoundException("Checkout with id " + " not found");
        }
        return ResponseEntity.ok().body(searched.get());
    }

    @GetMapping
    public ResponseEntity<List<CheckoutDto>> getByUserId(@RequestParam(value = "user-id") String userId) {
        List<CheckoutEntity> checkouts = service.getByUserId(userId);
        return ResponseEntity.ok().body(mapper.convertToDtoList(checkouts));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CheckoutDto> addItemToCart(@PathVariable Long id, @RequestBody List<CheckoutItemEntity> items) throws Exception {
        return ResponseEntity.ok().body(service.addItems(id, items));
    }

    @DeleteMapping("/{id}/items/{item-id}")
    public ResponseEntity<CheckoutDto> removeItemFromCart(@PathVariable("id") Long id,
                                                          @PathVariable("item-id") Long itemId) {
        return ResponseEntity.ok().body(service.removeItem(id, itemId));
    }

    @PatchMapping("/{id}/items/{item-id}")
    public ResponseEntity<CheckoutDto> modifyQuantity(@PathVariable("id") Long id,
                                                      @PathVariable("item-id") Long itemId,
                                                      @RequestBody int quantity) {
        return ResponseEntity.ok().body(service.modifyItemQuantity(id, itemId, quantity));
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<CheckoutDto> addPreferredAddress(@PathVariable Long id, @RequestBody AddressEntity address) {
        return ResponseEntity.ok().body(service.addPreferredAddress(id, address));
    }

    @PostMapping("/{id}/payment-method")
    public ResponseEntity<CheckoutDto> addPreferredPaymentMethod(@PathVariable Long id, @RequestBody PaymentMethodEntity paymentMethod) {
        return ResponseEntity.ok().body(service.addPreferredPaymentMethod(id, paymentMethod));
    }
}
