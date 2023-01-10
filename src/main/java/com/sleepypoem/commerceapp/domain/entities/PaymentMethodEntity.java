package com.sleepypoem.commerceapp.domain.entities;


import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PaymentMethodEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    public Long getId() {
        return id;
    }
}
