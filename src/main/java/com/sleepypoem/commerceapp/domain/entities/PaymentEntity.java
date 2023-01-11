package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "payments")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="user_id")
    @NotNull
    private String userId;

    @OneToOne(cascade = CascadeType.ALL)
    private ReceiptEntity receipt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", receipt='" + receipt + '\'' +
                ", status=" + status +
                '}';
    }
}
