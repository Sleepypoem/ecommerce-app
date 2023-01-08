package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;
import com.sleepypoem.commerceapp.services.CheckoutService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("checkout")
public class CheckoutController extends AbstractController<CheckoutDto, CheckoutEntity> {

    @Autowired
    CheckoutService service;
    @Override
    protected AbstractService<CheckoutDto, CheckoutEntity> getService() {
        return service;
    }

    @GetMapping
    public ResponseEntity<List<CheckoutDto>> getAll() {
        return ResponseEntity.ok().body(getAllInternal());
    }

    @PostMapping
    public ResponseEntity<CheckoutDto> create(@RequestBody CheckoutEntity checkout) throws MyValidationException {
        return ResponseEntity.created(URI.create("/checkout")).body(createInternal(checkout));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckoutDto> update(@PathVariable Long id, @RequestBody CheckoutEntity checkout) throws MyValidationException {
        return ResponseEntity.ok().body(updateInternal(id, checkout));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok().body("Checkout with id "+ " id has been deleted.");
    }
}
