package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractDto;
import com.sleepypoem.commerceapp.domain.entities.ProductEntity;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CheckoutItemDto extends AbstractDto<Long> {
    private ProductEntity product;
    private int quantity;

    @Override
    public String toString() {
        return "CheckoutItemDto{" +
                ", id=" + id +
                "product=" + product +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
