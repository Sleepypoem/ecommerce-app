package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.controllers.utils.Paginator;
import com.sleepypoem.commerceapp.domain.dto.CardDto;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentMethodDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.mappers.PaymentMethodMapper;
import com.sleepypoem.commerceapp.services.PaymentMethodService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Controller
@RequestMapping("payment-methods")
@Slf4j
public class PaymentMethodController extends AbstractController<PaymentMethodDto, PaymentMethodEntity, Long> {

    PaymentMethodService service;

    protected PaymentMethodController(PaymentMethodMapper mapper, PaymentMethodService service) {
        super(mapper);
        this.service = service;
    }

    @Override
    public AbstractService<PaymentMethodEntity, Long> getService() {
        return service;
    }

    @PostMapping
    public ResponseEntity<ResourceStatusResponseDto> create(@RequestBody CardDto cardDto) throws Exception {
        PaymentMethodEntity created = service.createCard(cardDto.getCardToken(), cardDto.getUserId());
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

    @GetMapping(params = {"user-id"}, produces = "application/json")
    public ResponseEntity<PaginatedDto<PaymentMethodDto>> getByUserId(@RequestParam(value = "user-id") String userId,
                                                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                                                      @RequestParam(value = "size", defaultValue = "10") int size,
                                                                      @RequestParam(value = "sort-by", defaultValue = "id") String sortBy,
                                                                      @RequestParam(value = "sort-order", defaultValue = "asc") String sortOrder) {
        Paginator<PaymentMethodEntity, PaymentMethodDto> paginator = new Paginator<>(mapper);
        return ResponseEntity.ok().body(paginator.getPaginatedDto(service.getAllPaginatedAndSortedByUserId(userId, page, size, sortBy, sortOrder), "payment-methods"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDto> update(@PathVariable Long id, @RequestBody CardDto cardDto) {
        PaymentMethodEntity paymentMethodEntity = service.updateCard(id, cardDto.getCardToken());
        return ResponseEntity.ok().body(mapper.convertToDto(paymentMethodEntity));
    }
}
