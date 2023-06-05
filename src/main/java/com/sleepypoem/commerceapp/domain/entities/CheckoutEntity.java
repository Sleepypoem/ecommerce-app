package com.sleepypoem.commerceapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "checkouts")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutEntity extends AbstractEntity<Long> {
    @Column(name = "user_id")
    @NotNull
    private String userId;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "checkout")
    @JsonManagedReference
    private List<CheckoutItemEntity> items;

    @OneToOne
    private AddressEntity address;

    @OneToOne
    private PaymentMethodEntity paymentMethod;

    @Enumerated(EnumType.STRING)
    private CheckoutStatus status;

    @Transient
    private BigDecimal total;

    public BigDecimal getTotal() {
        return items.stream()
                .map(item -> BigDecimal.valueOf(item.getProduct().getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public String toString() {
        return "{" + "id=" + id +
                ", userId='" + userId + '\'' +
                ", items=" + items +
                ", address=" + address +
                ", paymentMethod=" + paymentMethod +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", total=" + total +
                '}';
    }
}
