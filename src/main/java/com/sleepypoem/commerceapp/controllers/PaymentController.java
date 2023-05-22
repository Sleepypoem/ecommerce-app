package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractReadOnlyController;
import com.sleepypoem.commerceapp.domain.dto.PaymentDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.domain.mappers.PaymentMapper;
import com.sleepypoem.commerceapp.services.PaymentService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController extends AbstractReadOnlyController<PaymentDto, PaymentEntity> {

    private final PaymentService service;

    public PaymentController(PaymentService service, PaymentMapper mapper) {
        super(mapper);
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ResourceStatusResponseDto> processPayment(@RequestBody PaymentRequestDto paymentRequest) throws Exception {
        PaymentEntity payment = service.processPayment(paymentRequest);
        String message = "Created payment with id " + payment.getId();
        String url = "GET : /api/payments/" + payment.getId();
        return ResponseEntity.ok().body(new ResourceStatusResponseDto(String.valueOf(payment.getId()), message, url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> findOneById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @Override
    protected AbstractService<PaymentEntity> getService() {
        return service;
    }
}
