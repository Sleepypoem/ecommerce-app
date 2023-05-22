package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import com.sleepypoem.commerceapp.domain.entities.ReceiptEntity;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PaymentDto extends AbstractDto<Long> {
    private String userId;
    private ReceiptEntity receipt;
    private PaymentStatus status;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", receipt='" + receipt + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
