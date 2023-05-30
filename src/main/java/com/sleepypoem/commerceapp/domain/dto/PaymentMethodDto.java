package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@ToString
public class PaymentMethodDto extends AbstractDto<Long> {
    private String paymentId;
    private String userId;
    private String paymentType;

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
