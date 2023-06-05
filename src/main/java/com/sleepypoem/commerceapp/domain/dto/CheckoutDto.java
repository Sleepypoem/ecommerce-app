package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CheckoutDto extends AbstractDto<Long> {
    private String userId;
    private List<CheckoutItemDto> items;
    private AddressDto address;
    private PaymentMethodDto paymentMethod;
    private BigDecimal total;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CheckoutStatus status;

    @Override
    public String toString() {
        return "CheckoutDto{" +
                ", id=" + id +
                "userId='" + userId + '\'' +
                ", items=" + items +
                ", address=" + address +
                ", paymentMethod=" + paymentMethod +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", total=" + total +
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
