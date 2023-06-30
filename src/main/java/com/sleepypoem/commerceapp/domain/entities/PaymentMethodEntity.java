package com.sleepypoem.commerceapp.domain.entities;


import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodEntity extends AbstractEntity<Long> {

    @Column(name = "payment_id")
    @NotNull
    protected String paymentId;

    @Column(name = "user_id")
    @NotNull
    protected String userId;

    @Column(name = "payment_type")
    @NotNull
    protected String paymentType;

    @Column(name = "stripe_user_id")
    private String stripeUserId;

    @Column(name = "last_four")
    private String last4;

    private String brand;

    @Column(name = "exp_month")
    private String expMonth;

    @Column(name = "exp_year")
    private String expYear;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethodEntity that = (PaymentMethodEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "PaymentMethodEntity{" +
                "paymentId='" + paymentId + '\'' +
                ", userId='" + userId + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", stripeUserId='" + stripeUserId + '\'' +
                ", last4='" + last4 + '\'' +
                ", brand='" + brand + '\'' +
                ", expMonth='" + expMonth + '\'' +
                ", expYear='" + expYear + '\'' +
                ", id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
