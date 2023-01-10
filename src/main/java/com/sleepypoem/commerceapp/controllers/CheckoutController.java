package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.mappers.CheckoutMapper;
import com.sleepypoem.commerceapp.services.CheckoutService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("checkout")
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
    public ResponseEntity<CheckoutDto> create(@RequestBody CheckoutEntity checkout) throws Exception {
        return ResponseEntity.created(URI.create("/checkout")).body(createInternal(checkout));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckoutDto> update(@PathVariable Long id, @RequestBody CheckoutEntity checkout) throws Exception {
        return ResponseEntity.ok().body(updateInternal(id, checkout));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok().body("Checkout with id " + " id has been deleted.");
    }

    @GetMapping
    public ResponseEntity<CheckoutDto> getByUserId(@RequestParam(value = "user-id") String userId) {
        CheckoutEntity checkout = service.getByUserId(userId);
        return ResponseEntity.ok().body(mapper.convertToDto(checkout));
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
                                                      @RequestBody int quantity) throws Exception {
        return ResponseEntity.ok().body(service.modifyItemQuantity(id, itemId, quantity));
    }
}
