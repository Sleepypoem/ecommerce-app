package com.sleepypoem.commerceapp.domain.entities;


import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
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
    private String paymentId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "payment_type")
    private String paymentType;

    @Override
    public Long getId() {
        return null;
    }
}
