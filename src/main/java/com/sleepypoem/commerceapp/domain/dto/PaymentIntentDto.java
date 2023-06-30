package com.sleepypoem.commerceapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentIntentDto {

    private String status;

    private String paymentIntentId;

    private Long amount;
}
