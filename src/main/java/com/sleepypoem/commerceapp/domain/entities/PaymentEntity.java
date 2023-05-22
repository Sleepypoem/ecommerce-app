package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

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

    @OneToOne(cascade = CascadeType.ALL)
    private ReceiptEntity receipt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", receipt='" + receipt + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
