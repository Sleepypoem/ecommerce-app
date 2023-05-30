package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.controllers.abstracts.AbstractController;
import com.sleepypoem.commerceapp.controllers.utils.Paginator;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
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
    public ResponseEntity<PaymentMethodDto> update(@PathVariable Long id, @RequestBody PaymentMethodEntity paymentMethod) throws Exception {
        return ResponseEntity.ok().body(updateInternal(id, paymentMethod));
    }
}
