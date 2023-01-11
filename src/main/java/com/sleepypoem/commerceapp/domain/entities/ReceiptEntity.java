package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.domain.interfaces.IEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "receipts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptEntity implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userFirstName;

    private String userLastName;

    private String currencyType;

    private LocalDateTime createdAt;

    private PaymentStatus status;

    private String paymentMethod;

    @OneToOne
    private AddressEntity shippingAddress;

    private Double total;
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", createdAt=" + createdAt +
                ", status=" + status +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", shippingAddress=" + shippingAddress +
                ", total=" + total +
                '}';
    }
}
