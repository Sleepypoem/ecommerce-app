package com.sleepypoem.commerceapp.domain.entities;


import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodEntity extends AbstractEntity<Long> {

    @Column(name = "payment_id")
    @NotNull
    private String paymentId;

    @Column(name = "user_id")
    @NotNull
    private String userId;

    @Column(name = "payment_type")
    @NotNull
    private String paymentType;

    @Override
    public String toString() {
        return "PaymentMethodEntity{" +
                "id=" + id +
                ", paymentId='" + paymentId + '\'' +
                ", userId='" + userId + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
