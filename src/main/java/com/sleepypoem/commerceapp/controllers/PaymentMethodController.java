package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.domain.dto.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.exceptions.MyValidationException;
import com.sleepypoem.commerceapp.services.PaymentMethodService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URI;

@Controller
@RequestMapping("payment-methods")
public class PaymentMethodController extends AbstractController<PaymentMethodDto, PaymentMethodEntity> {

    @Autowired
    PaymentMethodService service;

    @Override
    protected AbstractService<PaymentMethodDto, PaymentMethodEntity> getService() {
        return service;
    }

    public ResponseEntity<PaymentMethodDto> create(@RequestBody PaymentMethodEntity paymentMethod) throws MyValidationException {
        return ResponseEntity.created(URI.create("/payment-methods")).body(createInternal(paymentMethod));
    }


}
