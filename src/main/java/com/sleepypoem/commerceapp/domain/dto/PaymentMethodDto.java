package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PaymentMethodDto extends AbstractDto<Long> {
    private String paymentId;
    private String userId;
    private String paymentType;
    private String last4;
    private String brand;
    private String expMonth;
    private String expYear;
    private String stripeUserId;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", paymentId='" + paymentId + '\'' +
                ", userId='" + userId + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
