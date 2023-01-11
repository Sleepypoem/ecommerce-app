package com.sleepypoem.commerceapp.services.payment.utils;

import com.sleepypoem.commerceapp.domain.dto.ReceiptDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.CheckoutItemEntity;
import com.sleepypoem.commerceapp.domain.entities.ReceiptEntity;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ReceiptBuilder {

    private ReceiptEntity.ReceiptEntityBuilder receipt;

    private String paymentType;

    public ReceiptBuilder(String paymentType) {
        this.paymentType = paymentType;
        receipt = ReceiptEntity.builder();
    }

    public ReceiptBuilder init() {
        receipt.paymentMethod(paymentType);
        receipt.currencyType("USD");

        return this;
    }

    public ReceiptBuilder addTimeStamps(LocalDateTime created) {
        receipt.createdAt(created);

        return this;
    }

    public ReceiptBuilder setStatus( String status) {
        receipt.status(PaymentStatus.valueOf(status));
        return this;
    }

    public ReceiptBuilder fillUserInfo(UserDto user) {
        receipt.userFirstName(user.getFirstName())
                        .userLastName(user.getLastName());
        return this;
    }

    public ReceiptBuilder fillShippingAddress(AddressEntity address) {
        receipt.shippingAddress(address);
        return this;
    }

    public ReceiptBuilder fillItemList(List<CheckoutItemEntity> items) {
        receipt.total(calculateTotal(items));
        return this;
    }

    private double calculateTotal(List<CheckoutItemEntity> items) {
        return items.stream().reduce(0d, (subtotal, item) -> subtotal + item.getProduct().getPrice(), Double::sum);
    }

    public ReceiptEntity build() {
        return receipt.build();
    }
}
