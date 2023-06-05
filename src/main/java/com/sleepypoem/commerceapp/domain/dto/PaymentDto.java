package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import com.sleepypoem.commerceapp.domain.enums.Currency;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class PaymentDto extends AbstractDto<Long> {
    private String userId;
    private PaymentStatus status;
    private CheckoutDto checkout;
    private String paymentProviderMessage;
    private Currency currency;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
