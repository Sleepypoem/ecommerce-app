package com.sleepypoem.commerceapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "checkout_items")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutItemEntity extends AbstractEntity<Long> {
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @NotNull
    private ProductEntity product;

    private int quantity;

    @ManyToOne
    @JoinColumn(name = "checkout_id", referencedColumnName = "id")
    @JsonBackReference
    private CheckoutEntity checkout;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckoutItemEntity that = (CheckoutItemEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
