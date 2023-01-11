package com.sleepypoem.commerceapp.domain.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sleepypoem.commerceapp.domain.enums.CheckoutStatus;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "checkouts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutEntity implements IEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "user_id")
    @NotNull
    private String userId;

    @OneToMany(cascade = CascadeType.ALL)
    private List<CheckoutItemEntity> items;

    @OneToOne
    private AddressEntity address;

    @OneToOne
    private PaymentMethodEntity paymentMethod;

    @Override
    public Long getId() {
        return id;
    }

    @Enumerated(EnumType.STRING)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CheckoutStatus status;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CheckoutEntity that = (CheckoutEntity) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", items=" + items +
                ", address=" + address +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}
