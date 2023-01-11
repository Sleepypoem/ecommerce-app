package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "payments")
@ToString
public class PaymentEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="user_id")
    @NotNull
    private String userId;

    @OneToOne
    private CheckoutEntity checkout;

    private  double total;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Override
    public Long getId() {
        return id;
    }
}
