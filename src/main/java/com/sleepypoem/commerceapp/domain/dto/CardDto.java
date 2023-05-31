package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardDto extends AbstractDto<Long> {

    private String paymentId;
    private String userId;
    private String paymentType;
    private String last4;
    private String brand;
    private String expMonth;
    private String expYear;
}
