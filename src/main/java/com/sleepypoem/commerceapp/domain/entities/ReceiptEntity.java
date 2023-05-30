package com.sleepypoem.commerceapp.domain.entities;

import com.sleepypoem.commerceapp.domain.abstracts.AbstractEntity;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@Table(name = "receipts")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptEntity extends AbstractEntity<Long> {

    private String userFirstName;

    private String userLastName;

    private String currencyType;

    private PaymentStatus status;

    private String paymentMethod;

    @OneToOne
    private AddressEntity shippingAddress;

    private Double total;


    @Override
    public String toString() {
        return "ReceiptEntity{" +
                "id=" + id +
                ", userFirstName='" + userFirstName + '\'' +
                ", userLastName='" + userLastName + '\'' +
                ", currencyType='" + currencyType + '\'' +
                ", status=" + status +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", shippingAddress=" + shippingAddress +
                ", total=" + total +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
