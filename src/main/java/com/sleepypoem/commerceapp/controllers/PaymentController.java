package com.sleepypoem.commerceapp.controllers;

import com.sleepypoem.commerceapp.annotations.security.IsAdminOrSuperUser;
import com.sleepypoem.commerceapp.controllers.abstracts.AbstractReadOnlyController;
import com.sleepypoem.commerceapp.controllers.utils.Paginator;
import com.sleepypoem.commerceapp.domain.dto.PaginatedDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.dto.ResourceStatusResponseDto;
import com.sleepypoem.commerceapp.domain.dto.entities.PaymentDto;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.domain.mappers.PaymentMapper;
import com.sleepypoem.commerceapp.exceptions.MyBadRequestException;
import com.sleepypoem.commerceapp.exceptions.MyInternalException;
import com.sleepypoem.commerceapp.services.PaymentService;
import com.sleepypoem.commerceapp.services.abstracts.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentController extends AbstractReadOnlyController<PaymentDto, PaymentEntity, Long> {

    private final PaymentService service;

    public PaymentController(PaymentService service, PaymentMapper mapper) {
        super(mapper);
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("#paymentRequest.userId == authentication.principal.id")
    public ResponseEntity<ResourceStatusResponseDto> processPayment(@RequestBody PaymentRequestDto paymentRequest) {
        PaymentEntity payment = service.startPayment(paymentRequest);
        String message = "Created payment with id " + payment.getId();
        String url = "GET : /api/payments/" + payment.getId();
        return ResponseEntity.ok().body(new ResourceStatusResponseDto(String.valueOf(payment.getId()), message, url));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("@paymentService.getOneById(#id).userId == authentication.principal.id")
    public ResponseEntity<ResourceStatusResponseDto> updatePaymentStatus(@PathVariable Long id, @RequestParam String action) {
        PaymentEntity payment;
        if (action.equals("cancel")) {
            payment = service.cancelPayment(id);
        } else if (action.equals("confirm")) {
            payment = service.confirmPayment(id);
        } else {
            throw new MyBadRequestException("Invalid action, must be cancel or confirm", null);
        }
        String message = generateMessageBasedOnStatus(payment);
        String url = "GET : /api/payments/" + id;
        return ResponseEntity.ok().body(new ResourceStatusResponseDto(String.valueOf(payment.getId()), message, url));
    }

    private String generateMessageBasedOnStatus(PaymentEntity payment) {
        return switch (payment.getStatus()) {
            case CANCELLED ->
                    "Payment with id " + payment.getId() + " was cancelled. Message: " + payment.getPaymentProviderMessage();
            case SUCCESS ->
                    "Payment with id " + payment.getId() + " was confirmed. Message: " + payment.getPaymentProviderMessage();
            case FAILED ->
                    "Payment with id " + payment.getId() + " failed, check card details and try again. Message: " + payment.getPaymentProviderMessage();
            default -> throw new MyInternalException("Unexpected value: " + payment.getStatus());
        };
    }

    @GetMapping
    @IsAdminOrSuperUser
    public ResponseEntity<Iterable<PaymentDto>> findAll() {
        return ResponseEntity.ok().body(getAllInternal());
    }

    @GetMapping(params = {"page", "size", "sortBy", "sortDirection"})
    @IsAdminOrSuperUser
    public ResponseEntity<PaginatedDto<PaymentDto>> getAllPaginatedAndSorted(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                                             @RequestParam(defaultValue = "ASC") String sortDirection) {
        return ResponseEntity.ok().body(getAllPaginatedAndSortedInternal(page, size, sortBy, sortDirection, "payments?"));
    }

    @GetMapping(params = {"user-id"})
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or #userId == authentication.principal.id")
    public ResponseEntity<PaginatedDto<PaymentDto>> getByUserIdPaginatedAndSorted(@RequestParam(value = "user-id") String userId,
                                                                                  @RequestParam(defaultValue = "0") int page,
                                                                                  @RequestParam(defaultValue = "10") int size,
                                                                                  @RequestParam(defaultValue = "id") String sortBy,
                                                                                  @RequestParam(defaultValue = "ASC") String sortDirection) {
        Paginator<PaymentDto> paginator = new Paginator<>("payments?user-id=" + userId + "&");
        Page<PaymentEntity> pagedResult = service.getAllPaginatedAndSortedByUserId(userId, page, size, sortBy, sortDirection);
        return ResponseEntity.ok().body(paginator.getPaginatedDtoFromPage(pagedResult, mapper));
    }

    @GetMapping("/{id}")
    @PostAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SUPERUSER') or returnObject.body.userId == authentication.principal.id")
    public ResponseEntity<PaymentDto> findOneById(@PathVariable Long id) {
        return ResponseEntity.ok().body(getOneByIdInternal(id));
    }

    @Override
    protected AbstractService<PaymentEntity, Long> getService() {
        return service;
    }
}
