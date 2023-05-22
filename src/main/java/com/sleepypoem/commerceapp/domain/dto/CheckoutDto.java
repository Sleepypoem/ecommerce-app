package com.sleepypoem.commerceapp.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CheckoutDto extends AbstractDto<Long> {
    private String userId;
    private List<CheckoutItemEntity> items;
    private AddressEntity address;
    private PaymentMethodEntity paymentMethod;

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
                '}';
    }
}
