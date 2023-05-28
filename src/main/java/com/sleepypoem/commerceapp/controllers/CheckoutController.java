package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.CheckoutItemDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.services.CheckoutService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("checkouts")
@Slf4j
public class CheckoutController extends AbstractController<CheckoutDto, CheckoutEntity> {

    private final CheckoutService service;

    protected CheckoutController(BaseMapper<CheckoutEntity, CheckoutDto> mapper, CheckoutService service) {
        super(mapper);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResourceStatusResponseDto> create(@RequestBody CheckoutEntity checkout) throws Exception {
        CheckoutDto created = createInternal(checkout);
        String message = "Checkout created with id " + created.getId();
        String url = "GET : /api/checkouts/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/checkout/" + created.getId()))
                .body(new ResourceStatusResponseDto(String.valueOf(created.getId()), message, url));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckoutDto> update(@PathVariable Long id, @RequestBody CheckoutEntity checkout) {
        return ResponseEntity.ok().body(updateInternal(id, checkout));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = deleteInternal(id);
        return ResponseEntity.ok().body(
                deleted ? "Checkout with id " + id + " has been deleted." : "Checkout with id " + id + " has not been deleted."
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckoutDto> findOneById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @GetMapping
    public ResponseEntity<List<CheckoutDto>> getByUserId(@RequestParam(value = "user-id") String userId) {
        List<CheckoutEntity> checkouts = service.findAllByUserId(userId);
        return ResponseEntity.ok().body(mapper.convertToDtoList(checkouts));
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CheckoutDto> addItemToCart(@PathVariable Long id, @RequestBody List<CheckoutItemEntity> items) {
        return ResponseEntity.ok().body(mapper.convertToDto(service.addItems(id, items)));
    }

    @DeleteMapping("/{id}/items/{item-id}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable("id") Long id,
                                                @PathVariable("item-id") Long itemId) {
        service.removeItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/items")
    public ResponseEntity<Void> removeAllItemsFromCart(@PathVariable("id") Long id) {
        service.cleanCart(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/items/{item-id}")
    public ResponseEntity<CheckoutDto> modifyQuantity(@PathVariable("id") Long id,
                                                      @PathVariable("item-id") Long itemId,
                                                      @RequestBody int quantity) {
        service.modifyItemQuantity(id, itemId, quantity);
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<List<CheckoutItemDto>> getItems(@PathVariable Long id) {
        return ResponseEntity.ok().body(getOneByIdInternal(id).getItems());
    }

    @PostMapping("/{id}/address")
    public ResponseEntity<CheckoutDto> addPreferredAddress(@PathVariable Long id, @RequestBody AddressEntity address) {
        return ResponseEntity.ok().body(mapper.convertToDto(service.addPreferredAddress(id, address)));
    }

    @PostMapping("/{id}/payment-method")
    public ResponseEntity<CheckoutDto> addPreferredPaymentMethod(@PathVariable Long id, @RequestBody PaymentMethodEntity paymentMethod) {
        return ResponseEntity.ok().body(mapper.convertToDto(service.addPreferredPaymentMethod(id, paymentMethod)));
    }

    @Override
    public AbstractService<CheckoutEntity> getService() {
        return service;
    }
}
