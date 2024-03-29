package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import com.sleepypoem.commerceapp.domain.enums.Currency;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
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
@Table(name = "payments")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity extends AbstractEntity<Long> {

    @Column(name = "user_id")
    @NotNull
    private String userId;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne
    private CheckoutEntity checkout;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Transient
    private String paymentProviderMessage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PaymentEntity{" +
                "userId='" + userId + '\'' +
                ", status=" + status +
                ", checkout=" + checkout +
                ", currency=" + currency +
                ", paymentProviderMessage='" + paymentProviderMessage + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
