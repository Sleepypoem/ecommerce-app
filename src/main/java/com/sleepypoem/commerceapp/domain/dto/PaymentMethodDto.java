package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PaymentMethodDto implements IDto {
    private Long id;
    private String paymentId;
    private String userId;
    private String paymentType;
}
