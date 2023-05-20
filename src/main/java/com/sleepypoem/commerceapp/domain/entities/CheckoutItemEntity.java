package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "checkout_items")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemEntity extends AbstractEntity<Long> {
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @NotNull
    private ProductEntity product;

    @PositiveOrZero
    private int quantity;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", product=" + product +
                ", quantity=" + quantity +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
