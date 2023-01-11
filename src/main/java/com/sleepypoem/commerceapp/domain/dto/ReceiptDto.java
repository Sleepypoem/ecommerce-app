package com.sleepypoem.commerceapp.domain.dto;

import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.domain.interfaces.IDto;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReceiptDto implements IDto {

    private Long id;

    private String userFirstName;

    private String userLastName;

    private String currencyType;

    private LocalDateTime createdAt;

    private PaymentStatus status;

    private String paymentMethod;
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
