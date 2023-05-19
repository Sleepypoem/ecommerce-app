package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.PaymentMethodMapper;
import com.sleepypoem.commerceapp.exceptions.MyResourceNotFoundException;
import com.sleepypoem.commerceapp.services.PaymentMethodService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("payment-methods")
public class PaymentMethodController extends AbstractController<PaymentMethodDto, PaymentMethodEntity> {

    @Autowired
    PaymentMethodService service;

    @Autowired
    PaymentMethodMapper mapper;

    @Override
    protected AbstractService<PaymentMethodDto, PaymentMethodEntity> getService() {
        return service;
    }

    @PostMapping
    public ResponseEntity<ResourceStatusResponseDto> create(@RequestBody PaymentMethodEntity paymentMethod) throws Exception {
        PaymentMethodDto created = createInternal(paymentMethod);
        String message = "Payment method created with id " + created.getId();
        String url = "GET : /api/payment-methods/" + created.getId();
        return ResponseEntity
                .created(URI.create("/api/payment-methods/" + created.getId()))
                .body(new ResourceStatusResponseDto(String.valueOf(created.getId()), message, url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> findOneById(@PathVariable Long id) {
        Optional<PaymentMethodDto> searched = getOneByIdInternal(id);
        if (searched.isEmpty()) {
            throw new MyResourceNotFoundException("Payment method with id " + " not found");
        }
        return ResponseEntity.ok().body(searched.get());
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDto>> getByUserId(@RequestParam(value = "user-id") String userId) {
        List<PaymentMethodEntity> paymentMethods = service.findByUserId(userId);
        return ResponseEntity.ok().body(mapper.convertToDtoList(paymentMethods));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> update(@PathVariable Long id, @RequestBody PaymentMethodEntity paymentMethod) throws Exception {
        return ResponseEntity.ok().body(updateInternal(id, paymentMethod));
    }
}
