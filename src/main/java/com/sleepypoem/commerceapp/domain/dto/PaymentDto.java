package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.entities.CheckoutEntity;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDto implements IDto {

    private Long id;
    private String userId;
    private CheckoutEntity checkout;

    private double total;
    private PaymentStatus status;

    public Long getId() {
        return id;
    }
}
