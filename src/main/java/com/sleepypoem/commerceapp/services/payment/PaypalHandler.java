package com.sleepypoem.commerceapp.services.payment;

import com.sleepypoem.commerceapp.domain.dto.CheckoutDto;
import com.sleepypoem.commerceapp.domain.dto.PaymentRequestDto;
import com.sleepypoem.commerceapp.domain.dto.UserDto;
import com.sleepypoem.commerceapp.domain.entities.AddressEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentEntity;
import com.sleepypoem.commerceapp.domain.entities.PaymentMethodEntity;
import com.sleepypoem.commerceapp.domain.entities.ReceiptEntity;
import com.sleepypoem.commerceapp.domain.enums.PaymentStatus;
import com.sleepypoem.commerceapp.services.payment.utils.ReceiptBuilder;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;


public class PaypalHandler implements IHandler {

    PaymentRequestDto paymentRequest;

    private CheckoutDto checkout;

    private AddressEntity address;

    private PaymentMethodEntity paymentMethod;

    private UserDto user;

    private PaymentStatus status;

    @Override
    public void setPaymentMethod(PaymentRequestDto paymentRequest) {
        this.paymentRequest = paymentRequest;
        this.checkout = paymentRequest.getCheckout();
        //this.address = paymentRequest.getCheckout().getAddress();
        //this.paymentMethod = paymentRequest.getCheckout().getPaymentMethod();
        this.user = paymentRequest.getUser();

    }

    @Override
    public PaymentEntity handle() {
        return PaymentEntity
                .builder()
                .userId(paymentRequest.getUser().getId())
                .receipt(pay())
                .status(status)
                .build();
    }

    @Override
    public boolean canHandle() {
        return Objects.equals(checkout.getPaymentMethod().getPaymentType(), "PAYPAL");
    }

    private ReceiptEntity pay() {
        return createPaymentIntent(paymentMethod.getPaymentId());
    }

    private ReceiptEntity createPaymentIntent(String paymentId) {
        //communicate with PayPal rest api

        LocalDateTime localDateTime = LocalDateTime.now();
        ReceiptBuilder receiptBuilder = new ReceiptBuilder("PAYPAL");
        validatePayment(paymentId);
        return receiptBuilder
                .init()
                .addTimeStamps(localDateTime)
                .setStatus(String.valueOf(status))
                //.fillItemList(checkout.getItems())
                .fillShippingAddress(address)
                .fillUserInfo(user)
                .build();
    }

    private void validatePayment(String paymentId) {
        PaymentStatus[] statuses = PaymentStatus.values();
        Random r = new Random();
        int rand = r.nextInt(4);
        System.out.println(rand);
        status = statuses[rand];
    }
}
