package com.sleepypoem.commerceapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    @JsonIgnore
    private String userId;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "checkout")
    @JsonManagedReference
    private List<CheckoutItemEntity> items = new ArrayList<>();

    @OneToOne
    private AddressEntity address;

    @OneToOne
    private PaymentMethodEntity paymentMethod;

    @Enumerated(EnumType.STRING)
    private CheckoutStatus status;

    @Transient
    private BigDecimal total = BigDecimal.ZERO;

    public BigDecimal getTotal() {
        return items.stream()
                .map(item -> BigDecimal.valueOf(item.getProduct().getPrice()).multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckoutEntity that = (CheckoutEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
