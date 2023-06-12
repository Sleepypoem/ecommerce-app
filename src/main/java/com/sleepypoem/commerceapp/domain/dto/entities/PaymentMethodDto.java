package com.sleepypoem.commerceapp.domain.dto.entities;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractDto<?> that = (AbstractDto<?>) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
