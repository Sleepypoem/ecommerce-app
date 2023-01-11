package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.domain.dto.PaymentDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceAddedResponseDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.exceptions.MyResourceNotFoundException;
import com.sleepypoem.commerceapp.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    PaymentService service;

    @PostMapping
    public ResponseEntity<ResourceAddedResponseDto> processPayment(@RequestBody PaymentRequestDto paymentRequest) throws Exception {
        PaymentDto payment = service.processPayment(paymentRequest);
        String message = "Created payment with id " + payment.getId();
        String url = "GET : /api/payments/" + payment.getId();
        return ResponseEntity.ok().body(new ResourceAddedResponseDto(String.valueOf(payment.getId()), message, url));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> findOneByUserId(@PathVariable Long id) {
        Optional<PaymentDto> searched = service.getOneById(id);
        if (searched.isEmpty()) {
            throw new MyResourceNotFoundException("Payment with id " + id + " not found.");
        }
        return ResponseEntity.ok().body(searched.get());
    }

    @PostMapping("/test")
    public ResponseEntity<PaymentDto> test(@RequestBody PaymentEntity paymentEntity) throws Exception {

        return ResponseEntity.ok().body(service.create(paymentEntity));
    }

}
