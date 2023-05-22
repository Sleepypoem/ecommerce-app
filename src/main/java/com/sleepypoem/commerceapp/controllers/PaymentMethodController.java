package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.BaseMapper;
import com.sleepypoem.commerceapp.services.PaymentMethodService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Controller
@RequestMapping("payment-methods")
public class PaymentMethodController extends AbstractController<PaymentMethodDto, PaymentMethodEntity> {

    PaymentMethodService service;

    protected PaymentMethodController(BaseMapper<PaymentMethodEntity, PaymentMethodDto> mapper, PaymentMethodService service) {
        super(mapper);
        this.service = service;
    }

    @Override
    public AbstractService<PaymentMethodEntity> getService() {
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

        return ResponseEntity.ok().body(getOneByIdInternal(id));
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
